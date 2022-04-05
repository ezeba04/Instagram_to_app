import instaloader

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

import os


ROOT_DIR = os.path.dirname(os.path.abspath("main.py"))
cred = credentials.Certificate(ROOT_DIR + '/bacherreciclerview-firebase-adminsdk-7r26r-5202bb305d.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

def uploadData(nombre, descripcion, urlImage):
    data = {
        u'nombre': nombre,
        u'descripcion': descripcion,
        u'urlImage': urlImage,
        u'creatorID': u'AdqnndfMlxZc6H09U8eoHKozjtY2'
    }

    db.collection(u'Recetas').add(data)


username = "geri.simon"

insta = instaloader.Instaloader()
profile = instaloader.Profile.from_username(insta.context, username)

posts = profile.get_posts()



for post in posts:
    postDate = post.date

    if postDate.year >= 2021 and postDate.month >= 9 and postDate.day >= 15:
        postDescription = post.caption

        if postDescription.__contains__('rutina') or postDescription.__contains__('sorteo'):
            print(postDescription[:100])
            subirPost = input("desea subir el post S/N?   ")

            if subirPost == "S":
                uploadData(post.caption,post.caption, post.url)
            elif subirPost == "N":
                print("continuar")

        else:
            nombre, desechable = postDescription.split(".",1)

            if post.is_video == True:
                URL = post.video_url
            else:
                URL = post.url
            uploadData(nombre, post.caption, URL)
            print(nombre)
            print(URL)
    else:
        break

print(profile.mediacount)