import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.File;
import java.util.Date;
import java.util.Scanner;


public class ClientProgram extends Listener {
    boolean n = false;

    static Client client;
    //IP сервера для подключения
    static String ip = "localhost";
    //Порт к которому мы будем подключатся
    static int tcpPort = 27960, udpPort = 27960;

    static boolean messageReceived = false;

    public static void main(String[] args) throws Exception {
        System.out.println("Подключаемся к серверу");
        //
        File directory = new File("./data");
        System.out.println(directory.getCanonicalPath());
        client = new Client();

        //Регистрируем пакет
        client.getKryo().register(PacketMessage.class);
        client.getKryo().register(Date.class);

        //Запускаем клиент
        client.start();
        //Клиент начинает подключатся к серверу

        //Клиент подключается к серверу
        client.connect(5000, ip, tcpPort, udpPort);

        client.addListener(new ClientProgram());

        System.out.println("Вы подключились к серверу! Клиент ждет получения пакета...n");
while(true){

}
        //while (!messageReceived) {
       //     Thread.sleep(1000);
       // }

        //System.out.println("Клиент покидает сервер");
        //System.exit(0);
    }


    public void received(Connection c, Object p) {
        //Проверяем какой отправляется пакет
        if (p instanceof PacketMessage) {
            //Если мы получили PacketMessage .
            PacketMessage packet = (PacketMessage) p;
            System.out.println("Ответ от сервера: " + packet.message);

            //Мы получили сообщение
            messageReceived = true;
        } else if (p instanceof Date) {
            new Thread(){
                @Override
                public void run() {
                    System.out.println(((Date) p).getHours() + " " + ((Date) p).getMinutes() + " " + ((Date) p).getSeconds());
                }
            }.start();
        }
    }
}
