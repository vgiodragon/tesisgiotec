import paho.mqtt.client as mqtt
from pymongo import MongoClient
import json
import datetime
import time
import _thread

clientMongo = MongoClient()
db = clientMongo.tesisgiotec

def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc) +" "+str(datetime.datetime.now()))
    client.subscribe("#")


# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    nmsg = str(msg.payload, 'utf-8')
    try:
        mjson = json.loads(nmsg)
        mjson['dateStore'] = str(datetime.datetime.now())
        if "Alarm1" in msg.topic:
            db.alarmaRaspberry.insert_one(mjson)
        elif "Alarm2" in msg.topic:
            db.alarmaOdroid.insert_one(mjson)
        elif "temperature" in msg.topic:
            db.temperature.insert_one(mjson)

    except ValueError as error:
        print("exp json"+str(error))

def connectar(cliente, ip):
    cliente.connect(ip, 1883, 60)
    cliente.loop_forever()

def main():
    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message
    username   = "CTIC-SMARTCITY"
    password   = "YTICTRAMS-CITC"
    client.username_pw_set(username, password)

    client2 = mqtt.Client()
    client2.on_connect = on_connect
    client2.on_message = on_message
    client2.username_pw_set(username, password)
    _thread.start_new_thread( connectar, (client, "192.168.1.103",) ) #publico en local!
    _thread.start_new_thread( connectar, (client2, "192.168.1.106",) ) #publico en local!

#    client.connect("localhost", 1883, 60)
    time.sleep(650)

if __name__ == "__main__":
    main()
