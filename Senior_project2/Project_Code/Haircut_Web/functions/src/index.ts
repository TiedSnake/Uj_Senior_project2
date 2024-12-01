import { initializeApp } from "firebase-admin/app";
import { auth, firestore} from "firebase-admin";
import { Timestamp} from "firebase-admin/firestore";
import { https } from "firebase-functions";

initializeApp();

export const generateLink = https.onCall(async (request) => {
  const { data, auth: contextAuth } = request;

  // Check if the user is authenticated
  if (!contextAuth) {
    return { error: "User is not authenticated" };
  }

  const userId = contextAuth.uid;
  const createdAt = data.createdAt;
  const expiresAt = data.expiresAt;
  const actionContext = data.context;

  // Validate required parameters
  if (!createdAt || !expiresAt || !actionContext) {
    return { error: "Missing required parameters" };
  } else if (!["EmailVerification", "PasswordReset"].includes(actionContext)) {
    return { error: "Invalid context" };
  }

  try {
    const userRecord = await auth().getUser(userId);
    const email = userRecord.email;
    if (!email) {
      throw new Error("Email not found for user");
    }
    console.log(`Verified user: ${email}`);

    const verificationCode = Math.floor(Math.random() * 900000 + 100000).toString();
    // let url;
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
        link = await auth().generatePasswordResetLink(email, actionCodeSettings);
        break;
      default:
        return { error: "Invalid context" };
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

    console.log(`A ${actionContext} link has been generated for ${userId} user with link ${link}`);
    // console.log(`A ${url} link has been generated for ${userId} user with link ${link}`);
    return { link, verificationCode };
    // return { url };
  } catch (error) {
    console.error("Error generating verification link:", error);
    return { error: "Failed to generate verification link" };
  }
});
