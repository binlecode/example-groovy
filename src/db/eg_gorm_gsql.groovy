package db

import groovy.sql.Sql

Testlet.withTransaction { status ->

def userId = 3
def nClient = Client.get(1)
def testletObj = Testlet.get('C5773598D80BE226E040A8C0F2C82EE4')

def sequenceNumber = 123456789
def itemKeyObj = ItemKey.get('C5773598D3A2E226E040A8C0F2C82EE4')

def sql = new Sql(ctx.dataSource_assessdomain)
/*
sql.execute('''
                        update kca_testlet_sequence set
                            MODIFIED_BY = :modified_by,
                            MODIFYSTAMP = :modifystamp,
                    CLIENT_ID = :client_id,
                            TESTLET_ID = :testlet_id,
                ITEM_KEY_ID = :item_key_id,
                            SEQUENCE_NUMBER = :sequence_number
                        where id = :id
                        ''', 
            [
                modified_by: userId, 
                modifystamp: new java.sql.Date(new Date().getTime()),
                client_id: nClient.id, 
                testlet_id: testletObj.id, 
                sequence_number: sequenceNumber, 
                item_key_id: itemKeyObj.id, 
                id: 'test-testlet-sequence-id'
            ]
            )
*/

sql.execute('''
                        insert into kca_testlet_sequence (id, created_by, createstamp, CLIENT_ID, TESTLET_ID, ITEM_KEY_ID,SEQUENCE_NUMBER)
            values (:id, :created_by, :createstamp, :client_id, :testlet_id, :item_key_id, :sequence_number)
                        ''', 
            [
                id: 'test-testlet-sequence-id', 
                created_by: userId,
                createstamp: new java.sql.Date(new Date().getTime()),
                client_id: nClient.id,
                testlet_id: testletObj.id, 
                item_key_id: itemKeyObj.id,
                sequence_number: sequenceNumber
            ]
            )

status.setRollbackOnly()


}


