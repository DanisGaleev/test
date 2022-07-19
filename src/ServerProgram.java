import java.util.Date;
import java.util.Scanner;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;


public class ServerProgram extends Listener {

    static Server server;
    Date date = new Date();
    static int udpPort = 27960, tcpPort = 27960; // Порт на котором будет работать наш сервер

    public static void main(String[] args) throws Exception {
        System.out.println("Создаем сервер");
        //Создаем сервер
        server = new Server();

        //Регистрируем пакет класс
        server.getKryo().register(PacketMessage.class);
        server.getKryo().register(Date.class);
        //Регистрируем порт
        server.bind(tcpPort, udpPort);

        //Запускаем сервер
        server.start();
        server.addListener(new ServerProgram());
    }

    //Используется когда клиент подключается к серверу
    public void connected(Connection c) {
        System.out.println("На сервер подключился " + c.getRemoteAddressTCP().getHostString());
        //Создаем сообщения пакета.
        PacketMessage packetMessage = new PacketMessage();
        //Пишем текст который будем отправлять клиенту.
        packetMessage.message = "Сейчас время: " + new Date().getHours() + ":" + new Date().getMinutes();

        //Отправляем текст
        c.sendTCP(packetMessage);
        // Так же можно отправить через UDP c.sendUDP(packetMessage);
    }

    //Используется когда клиент отправляет пакет серверу
    public void received(Connection c, Object p) {
        if (p instanceof String && p.equals("Время")) {
            //  c.sendTCP(new Date().getHours() + "  " + new Date().getMinutes());
            new Thread(){
                @Override
                public void run() {
                    c.sendTCP(date);
                }
            }.start();
        }
    }

    //Используется когда клиент покидает сервер.
    public void disconnected(Connection c) {
        System.out.println("Клиент покинул сервер!");
    }

    @Override
    public void idle(Connection connection) {
        super.idle(connection);
        connection.sendTCP(new Date());
    }
}
