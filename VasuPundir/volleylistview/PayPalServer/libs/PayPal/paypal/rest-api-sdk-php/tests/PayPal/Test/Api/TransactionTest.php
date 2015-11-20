<?php

namespace PayPal\Test\Api;

use PayPal\Api\Transaction;

class TransactionTest extends \PHPUnit_Framework_TestCase
{

    /** @var  Transaction */
    private $transaction;

    public static $description = "desc . . . ";
    public static $invoiceNumber = "#123";
    public static $custom = "custom";
    public static $softDescriptor = "descriptor";
    public static $total = "1.12";

    public static function createTransaction()
    {
        $transaction = new Transaction();
        $transaction->setAmount(AmountTest::createAmount());
        $transaction->setDescription(self::$description);
        $transaction->setInvoiceNumber(self::$invoiceNumber);
        $transaction->setCustom(self::$custom);
        $transaction->setSoftDescriptor(self::$softDescriptor);
        $transaction->setItemList(ItemListTest::createItemList());
        $transaction->setPayee(PayeeTest::createPayee());
        $transaction->setRelatedResources(array(RelatedResourcesTest::createRelatedResources()));
        return $transaction;
    }

    public function setup()
    {
        $this->transaction = self::createTransaction();
    }

    public function testGetterSetter()
    {
        $this->assertEquals(AmountTest::$currency, $this->transaction->getAmount()->getCurrency());
        $this->assertEquals(self::$description, $this->transaction->getDescription());
        $this->assertEquals(self::$invoiceNumber, $this->transaction->getInvoiceNumber());
        $this->assertEquals(self::$custom, $this->transaction->getCustom());
        $this->assertEquals(self::$softDescriptor, $this->transaction->getSoftDescriptor());
        $items = $this->transaction->getItemList()->getItems();
        $this->assertEquals(ItemTest::$quantity, $items[0]->getQuantity());
        $this->assertEquals(PayeeTest::$email, $this->transaction->getPayee()->getEmail());
        $resources = $this->transaction->getRelatedResources();
        $this->assertEquals(AuthorizationTest::$create_time, $resources[0]->getAuthorization()->getCreateTime());
    }

    public function testSerializeDeserialize()
    {
        $t1 = $this->transaction;

        $t2 = new Transaction();
        $t2->fromJson($t1->toJson());

        $this->assertEquals($t1, $t2);
    }
}
