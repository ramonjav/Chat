package firebase.app.pruebas2.gestion_ficheros;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static firebase.app.pruebas2.Utilidades.ListDatos.listusers;


public class Gestion_Ficheros {

        public static void guardardatos(Context context){

            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput("users.txt", Context.MODE_PRIVATE);
                ObjectOutputStream cos = new ObjectOutputStream(fos);
                cos.writeObject(listusers);
                cos.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void leerdatos(Context context){

            FileInputStream fis = null;
            try {
                fis = context.openFileInput("users.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
                listusers = (ArrayList)ois.readObject();
                ois.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

