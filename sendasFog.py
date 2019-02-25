import paho.mqtt.publish as publish
import _thread
import time
import datetime
import json
import sys
import threading

myauth = {'username':"Raspberry", 'password':"yrrebpsaR"}
sem = threading.Semaphore(230)

def publicar(topico,mensaje,server,myauth):
    try:
        sem.acquire()
        publish.single(topico, mensaje, hostname=server,auth = myauth)
        sem.release()
    except Exception as e:
        time.sleep(1)
        publicar(topico,mensaje,server,myauth)
        print ("Excepcion de MQTT "+str(e))

def create_publicar(nalarma,value):
    topico="giot"+str(nalarma)+"/prueba/temperature"
    data = {}
    data['value'] = value
    data['timestamp'] = str(datetime.datetime.now())
    try:
        _thread.start_new_thread( publicar, (topico, json.dumps(data), "localhost",myauth,) ) #publico en local!
    except Exception as e:
        time.sleep(1)
        _thread.start_new_thread( publicar, (topico, json.dumps(data), "localhost",myauth,) ) #publico en local!
    try:
        _thread.start_new_thread( publicar, (topico, json.dumps(data), "190.119.193.201",myauth,) ) #publico en global!
    except Exception as e:
        time.sleep(1)
        _thread.start_new_thread( publicar, (topico, json.dumps(data), "190.119.193.201",myauth,) ) #publico en global!

def main():
    talarmas = int(sys.argv[1])
    cont = 30.0
    time.sleep(5)
    print("Inicio Simulador Envios "+str(datetime.datetime.now()))
    for tanda in range(0,30):
        value = cont + (tanda%3)
        for nalarma in range(0,talarmas):
            create_publicar(nalarma,value)
        time.sleep(19-talarmas/(100))
    print("Fin Simulador Envios "+str(datetime.datetime.now()))

if __name__ == "__main__":
    main()
