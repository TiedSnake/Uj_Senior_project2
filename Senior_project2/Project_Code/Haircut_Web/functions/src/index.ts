// import { Request, Response } from "express";
import { config } from "dotenv"
import { randomInt } from "crypto";
import { https } from "firebase-functions";
import * as logger from "firebase-functions/logger";
import { initializeApp } from "firebase-admin/app";
import { auth } from "firebase-admin";
import { join } from "path";
import { readFile } from "fs";
import { getDatabase } from "firebase-admin/database";

// Load environment variables from .env file
config();

// Access environment variables
const firebaseConfig = {
  apiKey: process.env.FIREBASE_API_KEY,
  authDomain: process.env.FIREBASE_AUTH_DOMAIN,
  projectId: process.env.FIREBASE_PROJECT_ID,
  storageBucket: process.env.FIREBASE_STORAGE_BUCKET,
  messagingSenderId: process.env.FIREBASE_MESSAGING_SENDER_ID,
  appId: process.env.FIREBASE_APP_ID,
  measurementId: process.env.FIREBASE_MEASUREMENT_ID,
};

// Check if using the Firebase Emulator
const isLocal = process.env.FIREBASE_DATABASE_EMULATOR_HOST ? true : false;

// Initialize Firebase app
const app = isLocal ? initializeApp() : initializeApp(firebaseConfig);
const db = getDatabase(app);

//if the local emulator value is specified then run the cloud functions in the emulator.
if (isLocal && process.env.FIREBASE_DATABASE_EMULATOR_HOST) {
  const emulatorHost = process.env.FIREBASE_DATABASE_EMULATOR_HOST.split(":");
  const host = emulatorHost[0];
  const port = parseInt(emulatorHost[1], 10);

  // Connect to the Realtime Database emulator
  db.useEmulator(host, port);
}


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

exports.validateToken = https.onRequest(async (req, res) => {
  const code = req.query.verificationCode as string;
  const token = req.query.token as string;
  const context = req.query.context as string;
  if (!code) {
    logger.error("invalid code: The reset code is undefined");
    res.status(400).send("invalid-argument: invalid code: The reset code is undefined"); return;
  }
  if (!token) {
    logger.error("invalid-argument: invalid token: The token is undefined");
    res.status(400).send("invalid-argument: invalid token: The token is undefined"); return;
  }
  if (!context) {
    logger.error("invalid-argument: context is messing");
    res.status(400).send("invalid-argument: context is messing"); return;
  }

  try {
    const db = getDatabase();
    const tokenRef = db.ref(`verificationTokens/${token}`);
    const tokenSnapshot = await tokenRef.once("value");
    const tokenData = tokenSnapshot.val();

    if (!tokenData) {
      logger.error("error: token is not present in the database.");
      res.status(500).send("error: token is not present in the database."); return;
    }

    if (tokenData.valid && tokenData.verificationCode === code) {
      if (Date.now() <= tokenData.expiresAt) {
        let file;
        if (context === "emailVerification") {
          file = "verification.html";
        } else if (context === "passwordReset") {
          file = "passwordReset.html";
        } else {
          logger.error("error: unknown context");
          res.status(400).send("error: unknown context");
        }

        if (file) {
          const filePath = join(process.cwd(), "../src", file);
          sendHtmlFile(filePath, tokenData.verificaitonCode, res);
          await tokenRef.update({ valid: false });
        }
        // Invalidate the code
      } else {
        res.status(400).send("deadline-exceeded: The verification code has expired.");
      }
    } else if (tokenData.valid == false) {
      res.status(400).send("invalid-token: The token is invalid or have been used before");
    } else if (tokenData.verificationCode !== code) {
      res.status(400).send("invalid-verification-code: The verification code is invalid or have been used before");
    }
  } catch (error) {
    logger.error("internal server error", error);
    res.status(500).send("Internal server error");
  }
});

