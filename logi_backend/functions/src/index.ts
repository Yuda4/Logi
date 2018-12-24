import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import * as sha1 from 'sha1';

// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

export const helloWorld = functions.https.onRequest((request, response) => {
 response.send("Hello from Firebase!");
});

export const onProductCreate = functions.database
.ref('/products/{productId}')
.onCreate((snapshot, context) => {
    const productId = context.params.productId
    const sha1 = sha1(context.params.productId)
    return admin.database.ref('/products_approved').set({productId: sha1});
});