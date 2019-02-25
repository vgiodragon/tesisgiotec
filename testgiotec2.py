import paho.mqtt.client as mqtt
import _thread
import time
import datetime
import json
import sys
import threading

semL = threading.Semaphore(100)
semG = threading.Semaphore(100)

def publicar(client,topico,mensaje,sem):
    try:
        sem.acquire()
        ret = client.publish(topico,mensaje,qos=0)
        sem.release()
        if ret[0] != 0: #publish
            time.sleep(1)
            publicar(client,topico,mensaje)
    except Exception as e:
        print ("Excepcion de MQTT1 "+str(e))
        time.sleep(1)
        publicar(client,topico,mensaje)
        print ("Excepcion de MQTT "+str(e))

def create_publicar(client,client2,nalarma,value):
    topico="giot"+str(nalarma)+"/prueba/temperature"
    data = {}
    data['value'] = value
    data['timestamp'] = str(datetime.datetime.now())
    try:
        _thread.start_new_thread( publicar, (client,topico, json.dumps(data),semL,) ) #publico en local!
    except Exception as e:
        print ("Excepcion de _thread "+str(e))
        time.sleep(1)
        _thread.start_new_thread( publicar, (client,topico, json.dumps(data),semL,) ) #publico en local!
    try:
        _thread.start_new_thread( publicar, (client2,topico, json.dumps(data),semG,) ) #publico en global!
    except Exception as e:
        time.sleep(1)
        _thread.start_new_thread( publicar, (client2,topico, json.dumps(data),semG,) ) #publico en global!

def main():
    talarmas = int(sys.argv[1])
    username   = "CTIC-SMARTCITY"
    password   = "YTICTRAMS-CITC"
    maxhilos=25
    clientL = [None] * maxhilos
    clientG = [None] * maxhilos
    for hilo in range(0,maxhilos):
        clientL[hilo] = mqtt.Client()
        clientL[hilo].username_pw_set(username, password)
        #clientL[hilo].max_inflight_messages_set(200)
        clientL[hilo].connect("localhost") #connect to broker

        clientG[hilo] = mqtt.Client()
        clientG[hilo].username_pw_set(username, password)
        #clientG[hilo].max_inflight_messages_set(200)
        clientG[hilo].connect("190.119.192.232") #connect to broker

    cont = 30.0
    time.sleep(15)
    print("Inicio Simulador Envios "+str(datetime.datetime.now()))
    for tanda in range(0,30):
        value = cont + (tanda%3)
        for nalarma in range(0,talarmas):
            create_publicar(clientL[nalarma%maxhilos],clientG[nalarma%maxhilos],nalarma,value)
        time.sleep(20)
    print("Fin Simulador Envios "+str(datetime.datetime.now()))

if __name__ == "__main__":
    main()

