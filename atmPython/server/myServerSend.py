'''
Created on Feb 18, 2010

Altered Feb 20, 2014
'''

from struct import pack
from serverXMLpackager import XMLPackage as xml

version = '0.0.1'

def general_failure(conn, type, reason):

    #find the appropriate opcode to send for particular errors
    if type == 'create':
        typebyte = '\x12'
    elif type == 'delete':
        typebyte = '\x22'
    elif type == 'deposit':
        typebyte = '\x32'
    elif type == 'withdraw':
        typebyte = '\x42'
    elif type == 'balance':
        typebyte = '\x52'

    #encode and send the string
    #utf = reason.encode('utf-8')
    #utflen = len(utf)
    #conn.send('\x01' + pack('!I',2 + utflen) + typebyte + pack('!h',utflen) + utf)
    conn.send(version, typebyte, reason.encode('utf-8'), True)
    return

#create new account
def create_success(conn,act):
    #xml = server_package(version, 4, act, False)
    #conn.send('\x01' + pack('!I',4) +'\x11' + pack('!I',act))
    conn.send(server_package(version, '\x11', act, False)
    return

#delete an existing account
def delete_success(conn):
    #conn.send('\x01\x00\x00\x00\x00\x21')
    conn.send(server_package(version, '\x21', "account deleted", False))
    return

#deposit to an existing account
def deposit_success(conn,bal):
    #conn.send('\x01' + pack('!I',4) +'\x31' + pack('!I',bal))
    conn.send(server_package(version, '\x31', bal, False)
    return

#withdraw from an existing account
def withdraw_success(conn,bal):
    #conn.send('\x01' + pack('!I',4) +'\x41' + pack('!I',bal))
    conn.send(server_package(version, '\x41', bal, False)
    return

#withdraw from an existing account
def balance_success(conn,bal):
    #conn.send('\x01' + pack('!I',4) +'\x51' + pack('!I',bal))
    conn.send(server_package(version, '\x51', bal, False)
    return

#end a session
def end_session_success(conn):
    #conn.send('\x01\x00\x00\x00\x00\x61')
    conn.send(server_package(version, '\x61', bal, False)
    return

#handle invalid opcodes
def unknown_opcode(conn):
    #conn.send('\x01\x00\x00\x00\x00\x62')
    conn.send(server_package(version, '\x61', "unknown opcode", False)
    return


