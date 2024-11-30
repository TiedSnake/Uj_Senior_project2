// import { Request, Response } from "express";
import { config } from "dotenv"
import { https } from "firebase-functions";
// import * as logger from "firebase-functions/logger";
import { initializeApp } from "firebase-admin/app";
import { auth, firestore } from "firebase-admin";
// import { getDatabase } from "firebase-admin/database";

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
initializeApp();
// const db = getDatabase(app);

// if the local emulator value is specified then run the cloud functions in the emulator.
// if (isLocal && process.env.EMULATOR_LOCAL_IP_ADDRESS) {
//   const emulatorHost = process.env.EMULATOR_LOCAL_IP_ADDRESS.split(":");
//   const host = emulatorHost[0];
//   const port = parseInt(emulatorHost[1], 10);
//   // Connect to the Realtime Database emulator
//   db.useEmulator(host, port);
// }

interface VerificationRequest {
  email: string;
  createdAt: string; // ISO or timestamp
  expiresAt: string; // ISO or timestamp
}

enum VerificationContext {
  EmailVerification = "emailVerification",
  ResetPassword = "resetPassword",
}

export const verify = https.onRequest(async (req, res) => {
  if (req.method !== "POST") {
    res.status(405).send("Method not allowed");
    return;
  }

  const userToken = req.headers["authorization-token"] as string;
  const context = req.headers["context"] as string;

  if (!userToken || !context) {
    res.status(400).send("Missing headers: token or context");
    return;
  }

  const { email, createdAt, expiresAt } = req.body as VerificationRequest;
  if (!email || !createdAt || !expiresAt) {
    res.status(400).send("Missing required fields");
    return;
  }

  try {
    const decodedToken = await auth().verifyIdToken(userToken);
    console.log(`Verified user: ${decodedToken.email}`);

    const verificationCode = Math.floor(Math.random() * 900000 + 100000).toString();
    // let actionCodeSettings: { url: string; handleCodeInApp: boolean };
    let url;
    switch (context) {
      case VerificationContext.EmailVerification:
        url = `http://127.0.0.1:5000/verification.html?verificationCode=${verificationCode}`
        // actionCodeSettings = {
        //   url: `http://127.0.0.1:5000/verification.html?verificationCode=${verificationCode}`,
        //   handleCodeInApp: false,
        // };
        break;
      case VerificationContext.ResetPassword:
        url = `http://127.0.0.1:5000/passwordReset.html?verificationCode=${verificationCode}`
        // actionCodeSettings = {
        //   url: `http://192.168.8.101:5000/passwordReset.html?verificationCode=${verificationCode}`,
        //   handleCodeInApp: false,
        // };
        break;
      default:
        res.status(400).send("Invalid context");
        return;
    }

    // const link = await auth().generateEmailVerificationLink(email, actionCodeSettings);
    // console.log(`Verification link for ${email}: ${link}`);
    console.log(`Verification link for ${email}: ${url}`);

    const tokenObject = {
      user_email: email,
      userId: decodedToken.uid,
      verificationCode,
      userToken,
      createdAt: new Date(createdAt).toISOString(),
      expiresAt: new Date(expiresAt).toISOString(),
    };

    await firestore().collection("verifications").add(tokenObject);

    // Firestore cleanup
    const snapshot = await firestore()
      .collection("verifications")
      .where("createAt", ">", expiresAt)
      .get();
    const batch = firestore().batch();
    snapshot.forEach(doc => batch.delete(doc.ref));
    await batch.commit();

    // res.status(200).send({ link, verificationCode });
    res.status(200).send({ url});
  } catch (error) {
    console.error("Error generating verification link:", error);
    res.status(500).send({ error: "Failed to generate verification link" });
  }
});






// exports.verify = https.onRequest(async (req, res) => {
//   if (req.method !== "POST") {
//     res.status(400).send("Unauthorized access");
//     return
//   }

//   const userToken = req.headers.token as string;
//   if (!userToken) {
//     res.status(400).send("missing token or context");
//     return
//   }
//   const context = req.headers.context as string;
//   if (!context) {
//     res.status(400).send("missing context");
//     return
//   }
//   const email = req.body.email as string;
//   if (!email) {
//     res.status(400).send("missing user email");
//     return
//   }
//   const createAt = req.body.createAt as Date;
//   if (!createAt) {
//     res.status(400).send("missing user created date");
//     return
//   }
//   const expiresAt = req.body.expiresAt as Date;
//   if (!expiresAt) {
//     res.status(400).send("missing user created date");
//     return
//   }
//   try {
//     const decodedToken = await auth().verifyIdToken(userToken);
//     console.log(`Verified user: ${decodedToken.email}`);

//     const userId = decodedToken.uid;
//     const verificationCode = randomInt(100000, 999999).toString();

//     let actionCodeSettings;

//     if (context === "emailVerification") {
//       actionCodeSettings = {
//         url: `http://192.168.8.101:5001/verification.html?verificationCode=${verificationCode}`,
//         handleCodeInApp: false,
//       };
//     } else if (context === "resetPassword") {
//       actionCodeSettings = {
//         url: `http://192.168.8.101:5001/passwordReset.html?verificationCode=${verificationCode}`,
//         handleCodeInApp: false,
//       };
//     } else {
//       res.status(400).send("Unknown context specified");
//     }
//     const link = await auth().generateEmailVerificationLink(email, actionCodeSettings)

//     console.log(`Verification link for ${email}: ${link}`);

//     const tokenObject = {
//       user_email: email,
//       userId: userId,
//       userToken: userToken,
//       createdAt: createAt,
//       expiresAt: expiresAt
//     }
//     await firestore().collection("verifications").add(tokenObject)

//     await auth().generateEmailVerificationLink
//     const snapshot = await firestore()
//       .collection("verifications")
//       .where("createAt", ">", expiresAt)
//       .get();
//     snapshot.forEach(doc => doc.ref.delete());
//   } catch (error) {
//     console.error("Error generating verification number: ", error);
//     res.status(500).send("Unable to generate verification number");
//   }
// })

//Persist verification code in Firestore
// await firestore().collection("verifications").doc(userId).set({
//   verificationNumber: verificationCode,
//   createAt: firestore.FieldValue.serverTimestamp(),
// });

//Determine which HTML file to forward the user to