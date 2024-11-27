// import { Request, Response } from "express";
import { config } from "dotenv"
import { randomInt } from "crypto";
import { https } from "firebase-functions";
import * as logger from "firebase-functions/logger";
import { initializeApp } from "firebase-admin/app";
import { auth, firestore } from "firebase-admin";
import { readFile } from "fs";
import { getDatabase } from "firebase-admin/database";
import path = require("node:path/win32");

// Load environment variables from .env file
config();

// Access environment variables
// const firebaseConfig = {
//   apiKey: process.env.FIREBASE_API_KEY,
//   authDomain: process.env.FIREBASE_AUTH_DOMAIN,
//   projectId: process.env.FIREBASE_PROJECT_ID,
//   storageBucket: process.env.FIREBASE_STORAGE_BUCKET,
//   messagingSenderId: process.env.FIREBASE_MESSAGING_SENDER_ID,
//   appId: process.env.FIREBASE_APP_ID,
//   measurementId: process.env.FIREBASE_MEASUREMENT_ID,
// };

// Check if using the Firebase Emulator
// const isLocal = process.env.EMULATOR_LOCAL_IP_ADDRESS ? true : false;

// Initialize Firebase app
const app = initializeApp();
const db = getDatabase(app);

//if the local emulator value is specified then run the cloud functions in the emulator.
// if (isLocal && process.env.EMULATOR_LOCAL_IP_ADDRESS) {
//   const emulatorHost = process.env.EMULATOR_LOCAL_IP_ADDRESS.split(":");
//   const host = emulatorHost[0];
//   const port = parseInt(emulatorHost[1], 10);

//   // Connect to the Realtime Database emulator
//   db.useEmulator(host, port);
// }


exports.generateCustomToken = https.onRequest(async (req, res) => {
  if (req.method !== "POST") {
    res.status(405).send("Method not allowed");
    return
  }

  const uid = req.body.uid;
  if (!uid) {
    res.status(401).send("Request must be authenticated.");
    return;
  }

  try {
    // Generate a unique verification code (e.g., a 6-digit numeric code)
    const verificationCode = randomInt(100000, 999999).toString();

    // Create the custom token
    const customToken = await auth().createCustomToken(uid); // expires in 15 minutes 15*60=900sec

    // Store the custom token and verification code in the database

    await db.ref(`verificationTokens/${uid}/${customToken}`).set({
      uid: uid,
      verificationCode: verificationCode,
      valid: true,
      createdAt: Date.now(), // for expiration purposes
      expiresAt: (Date.now() + (15 * 60 * 1000)),
    });

    // Return =the token
    res.status(200).json({ customToken, verificationCode });
  } catch (error) {
    console.error("Error generating token: ", error);
    res.status(500).send("Unable to generate token");
  }
});

const sendHtmlFile = (filePath: string, verificaitonCode: string, res: any) => {
  readFile(filePath, "utf8", (err, data) => {
    if (err) {
      logger.error("Error reading HTML file: ", err);
      return res.status(500).send("Internal Server Error");
    }
    const responseHtml = data.replace("{{verificationCode}}", verificaitonCode);
    res.setHeader("Content-Type", "text/html");
    res.send(responseHtml);
    return;
  });
};

exports.verify = https.onRequest(async (req, res) => {
  const userToken = req.query.token as string;
  // const context = req.query.context as string;
  if (!userToken ) {
    res.status(400).send("missing token or context");
    return
  }

  try {
    const decodedToken = await auth().verifyIdToken(userToken);
    const userId = decodedToken.uid;
    const verificationCode = randomInt(100000, 999999).toString();

    //Persist verification code in Firestore
    await firestore().collection("verifications").doc(userId).set({
      verificationNumber: verificationCode,
      createAt: firestore.FieldValue.serverTimestamp(),
    });

    //Determine which HTML file to forward the user to
    // const file = context === "emailVerification" ? "verification.html" :
    //   context === "resetPassword" ? "passwordReset.html" : null;
      const file = "verification.html";
    if (!file) {
      res.status(400).send("error: unknwon context");
      return;
    }
    const filePath = path.resolve(__dirname, "public", file);
    // const filePath = join(process.cwd(), "../public", file);
    sendHtmlFile(filePath, verificationCode, res);

    //Clean expired codes from Firestore
    const expireTime = new Date();
    expireTime.setMinutes(expireTime.getMinutes() - 10); // Expire after 10 minutes

    const snapshot = await firestore()
      .collection("verifications")
      .where("createAt", "<", expireTime)
      .get();

    snapshot.forEach(doc => doc.ref.delete());
  } catch (error) {
    console.error("Error generating verification number: ", error);
    res.status(500).send("Unable to generate verification number");
  }
})