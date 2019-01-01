import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp(functions.config().firebase);
//import * as sha1 from 'sha1';

// Start writing Firebase Functions
// C

// export const helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

// export const onProductCreate = functions.database
// .ref('/products/{productId}')
// .onCreate((snapshot, context) => {
//     const productId = context.params.productId
//     const sha1 = sha1(context.params.productId)
//     return admin.database.ref('/products_approved').set({productId: sha1});
// });

// exports.sendNotification = functions.database
// .ref('/deliveries/{storageId}/{deliveryId}')
// .onCreate(async(snapshot, context) => {
//     // store the values written to 'deliveries'
//     const deliveryInfo = snapshot.val()
//     // store the storage ID
//     const storageId = context.params.storageId
//     //store customer email
//     const customerEmail = replaceDot(deliveryInfo.customer_email)

//     const customer_token = snapshot.ref.parent.parent.parent.child('messages').child(customerEmail).child('messaging_token')

//     const companyName = snapshot.ref.parent.parent.parent.child('users').child(storageId).child('name')

//     const payload = {
//         notification: {
//             title: companyName,
//             text: 'New delivery accepted and pending for your time selection',
//             click_action: 'MAINACTIVITY'
//         }
//     }
    // return admin.messaging().sendToDevice(customer_token, payload)
    // .then((response) => {
    //     console.log("Payload sent: ", payload, " to token ", customer_token);
    //     console.log("Succsessfully sent message: ", response);
    // }).catch(function(error){
    //     console.log("Error sending message: ", error);
    // });
// });

function replaceDot(text: string): string{
    return text.replace(/./g, '_')
}

// Listens for new messages added to messages/:pushId
exports.pushNotification = functions.database.ref('/deliveries/{storageId}/{deliveryId}').onCreate( async(snapshot, context) => {

    console.log('Push notification event triggered');

    //  Grab the current value of what was written to the Realtime Database.
    const valueObject = snapshot.val();
    const body = 'New delivery accepted and pending for your time selection'

  // Create a notification
    const payload = {
        notification: {
            title: valueObject.delivery_id,
            text: body,
            click_action: 'MAINACTIVITY'
        },
    };

  //Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };

    const customerEmail = replaceDot(valueObject.customer_email)
    return admin.database().ref('/messages/' + customerEmail+'/')
    .once('value')
    .then(async(snap) => {
        const token = snap.val()
        console.log("token", token, "snap", snap.val())
        return admin.messaging().sendToDevice(token.messaging_token, payload, options)
        .then((response) => {
            console.log("Payload sent: ", payload, " to token ", token);
            console.log("Succsessfully sent message: ", response);
        }).catch(function(error){
            console.log("Error sending message: ", error);
        });
    })
})
