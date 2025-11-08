const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const db = admin.firestore();

/**
 * Set user as admin (only callable by super admin)
 */
exports.setAdminRole = functions.https.onCall(async (data, context) => {
  const superAdminUid = "PUT_YOUR_SUPERADMIN_UID_HERE";
  if (!context.auth || context.auth.uid !== superAdminUid) {
    throw new functions.https.HttpsError("permission-denied", "Not allowed");
  }
  const uid = data.uid;
  await admin.auth().setCustomUserClaims(uid, { role: "admin" });
  await db.collection("users").doc(uid).set({ role: "admin" }, { merge: true });
  return { success: true };
});

/**
 * Log applications automatically
 */
exports.onApplicationCreated = functions.firestore
  .document("applications/{appId}")
  .onCreate(async (snap, context) => {
    const app = snap.data();
    await db.collection("admin_audit").add({
      action: "application_created",
      jobId: app.jobId,
      userId: app.applicantUid,
      timestamp: admin.firestore.FieldValue.serverTimestamp()
    });
    return null;
  });
