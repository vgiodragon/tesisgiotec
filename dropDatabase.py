from pymongo import MongoClient

clientMongo = MongoClient()
clientMongo.drop_database("tesisgiotec")
