import { initializeApp } from "firebase-admin/app";
import { auth, firestore } from "firebase-admin";
import { Timestamp } from "firebase-admin/firestore";
import { https } from "firebase-functions";

initializeApp();
export const generateLink = https.onRequest(async (req, res) => {
  if (req.method !== "POST") {
    res.status(405).send("Method not allowed");
    return;
  }
  const email = req.headers["email"] as string;
  const actionContext = req.headers["context"] as string;
  // const userToken = req.headers["authorization-token"] as string;
  const createdAt = req.body.createdAt;
  const expiresAt = req.body.expiresAt;
  if (!createdAt || !expiresAt || !actionContext) {
    res.status(400).send("Missing required parameters");
  } else if (!["EmailVerification", "PasswordReset"].includes(actionContext)) {
    res.status(400).send("Invalid context");
  }
  if (!email) {
    res.status(400).send("Email is undefined");
  }
  const user = await auth().getUserByEmail(email);
  try {
    const userId = user.uid;
    const verificationCode = Math.floor(Math.random() * 900000 + 100000).toString();
    let link;
    let actionCodeSettings: { url: string; handleCodeInApp: boolean };
    switch (actionContext) {
      // url = `http://127.0.0.1:5000/verification.html?verificationCode=${verificationCode}`
      case "EmailVerification":
        actionCodeSettings = {
          url: `https://haircut-93a44.web.app/verification.html?verificationCode=${verificationCode}`,
          handleCodeInApp: false,
        };
        link = await auth().generateEmailVerificationLink(email, actionCodeSettings);
        break;
      case "PasswordReset":
        // url = `http://127.0.0.1:5000/passwordReset.html?verificationCode=${verificationCode}`
        actionCodeSettings = {
          url: `https://haircut-93a44.web.app/passwordReset.html?verificationCode=${verificationCode}`,
          handleCodeInApp: false,
        };
        link = await auth().generateEmailVerificationLink(email, actionCodeSettings);
        break;
      default:
        res.status(400).send("Invalid context");
    }
    const tokenObject = {
      user_email: email,
      userId,
      verificationCode,
      createdAt: Timestamp.fromMillis(createdAt),
      expiresAt: Timestamp.fromMillis(expiresAt),
    };
    await firestore().collection("verifications").add(tokenObject);
    // Firestore cleanup
    const snapshot = await firestore()
        .collection("verifications")
        .where("createdAt", ">", expiresAt)
        .get();
    const batch = firestore().batch();
    snapshot.forEach((doc) => batch.delete(doc.ref));
    await batch.commit();

    // console.log(`A ${url} link has been generated for ${userId} user with link ${link}`);
    console.log(`A ${actionContext} link has been generated for ${userId} user with link ${link}`);
    // res.status(200).send(`A ${url} link has been generated for ${userId} user with link ${link}`);
    res.status(200).send(link);
  } catch (error) {
    console.error("Error generating verification link:", error);
    res.status(500).send({ error: "Failed to generate verification link" });
  }
});
