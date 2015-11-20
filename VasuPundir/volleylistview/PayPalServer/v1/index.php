
<?php

ini_set('display_errors', 1);
require_once '../include/DBHandler.php';
require '../libs/Slim/Slim.php';

require __DIR__ . '/../libs/PayPal/autoload.php';

use PayPal\Api\Payment;

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

$userId = 1;

/**
 * Echoing json response to client
 * @param String $status_code Http response code
 * @param Int $response Json response
 */
function echoResponse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);

    // setting response content type to json
    $app->contentType('application/json');

    echo json_encode($response);
}

function authenticate(\Slim\Route $route) {
    // Implement your user authentication here
    // Check http://www.androidhive.info/2014/01/how-to-create-rest-api-for-android-app-using-php-slim-and-mysql-day-23/
}

/**
 * Lists all products
 * method - GET
 */
$app->get('/products', 'authenticate', function() {
            $response = array();
            $db = new DbHandler();

            // fetching all products
            $result = $db->getAllProducts();

            $response["error"] = false;
            $response["products"] = array();

            // looping through result and preparing products array
            while ($task = $result->fetch_assoc()) {
                $tmp = array();
                $tmp["id"] = $task["id"];
                $tmp["name"] = $task["name"];
                $tmp["price"] = $task["price"];
                $tmp["description"] = $task["description"];
                $tmp["image"] = $task["image"];
                $tmp["sku"] = $task["sku"];
                $tmp["created_at"] = $task["created_at"];
                array_push($response["products"], $tmp);
            }

            echoResponse(200, $response);
        });

/**
 * verifying the mobile payment on the server side
 * method - POST
 * @param paymentId paypal payment id
 * @param paymentClientJson paypal json after the payment
 */
$app->post('/verifyPayment', 'authenticate', function() use ($app) {

            $response["error"] = false;
            $response["message"] = "Payment verified successfully";
            global $userId;


            require_once '../include/Config.php';

            try {
                $paymentId = $app->request()->post('paymentId');
                $payment_client = json_decode($app->request()->post('paymentClientJson'), true);

                $apiContext = new \PayPal\Rest\ApiContext(
                        new \PayPal\Auth\OAuthTokenCredential(
                        PAYPAL_CLIENT_ID, // ClientID
                        PAYPAL_SECRET      // ClientSecret
                        )
                );

                // Gettin payment details by making call to paypal rest api
                $payment = Payment::get($paymentId, $apiContext);

                // Verifying the state approved
                if ($payment->getState() != 'approved') {
                    $response["error"] = true;
                    $response["message"] = "Payment has not been verified. Status is " . $payment->getState();
                    echoResponse(200, $response);
                    return;
                }

                // Amount on client side
                $amount_client = $payment_client["amount"];

                // Currency on client side
                $currency_client = $payment_client["currency_code"];

                // Paypal transactions
                $transaction = $payment->getTransactions()[0];
                // Amount on server side
                $amount_server = $transaction->getAmount()->getTotal();
                // Currency on server side
                $currency_server = $transaction->getAmount()->getCurrency();
                $sale_state = $transaction->getRelatedResources()[0]->getSale()->getState();

                // Storing the payment in payments table
                $db = new DbHandler();
                $payment_id_in_db = $db->storePayment($payment->getId(), $userId, $payment->getCreateTime(), $payment->getUpdateTime(), $payment->getState(), $amount_server, $amount_server);

                // Verifying the amount
                if ($amount_server != $amount_client) {
                    $response["error"] = true;
                    $response["message"] = "Payment amount doesn't matched.";
                    echoResponse(200, $response);
                    return;
                }

                // Verifying the currency
                if ($currency_server != $currency_client) {
                    $response["error"] = true;
                    $response["message"] = "Payment currency doesn't matched.";
                    echoResponse(200, $response);
                    return;
                }

                // Verifying the sale state
                if ($sale_state != 'completed') {
                    $response["error"] = true;
                    $response["message"] = "Sale not completed";
                    echoResponse(200, $response);
                    return;
                }

                // storing the saled items
                insertItemSales($payment_id_in_db, $transaction, $sale_state);

                echoResponse(200, $response);
            } catch (\PayPal\Exception\PayPalConnectionException $exc) {
                if ($exc->getCode() == 404) {
                    $response["error"] = true;
                    $response["message"] = "Payment not found!";
                    echoResponse(404, $response);
                } else {
                    $response["error"] = true;
                    $response["message"] = "Unknown error occurred!" . $exc->getMessage();
                    echoResponse(500, $response);
                }
            } catch (Exception $exc) {
                $response["error"] = true;
                $response["message"] = "Unknown error occurred!" . $exc->getMessage();
                echoResponse(500, $response);
            }
        });

/**
 * method to store the saled items in sales table
 */
function insertItemSales($paymentId, $transaction, $state) {

    $item_list = $transaction->getItemList();

    $db = new DbHandler();

    foreach ($item_list->items as $item) {
        $sku = $item->sku;
        $price = $item->price;
        $quanity = $item->quantity;

        $product = $db->getProductBySku($sku);

        // inserting row into sales table
        $db->storeSale($paymentId, $product["id"], $state, $price, $quanity);
    }
}

$app->run();
?>