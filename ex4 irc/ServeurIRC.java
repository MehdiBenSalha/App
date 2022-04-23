
import java.net.*;
import java.io.*;
import java.util.*;

class ServeurIRC{

Vector V;
public static void main (String args[])
{ int port = 1973;
if( args.length == 1 )
port = Integer.parseInt( args [ 0 ] );
new ServeurIRC(port);
}

public ServeurIRC (int port)
{
V=new Vector();


try
{
ServerSocket server=new ServerSocket(port);
while (true){
            try {
                Socket socket = server.accept();
                System.out.println("Un nouveau utulisateur est arrivé");
                ThreadClient neo= new ThreadClient (socket, this);
                neo.start();
            } catch (IOException e) {
                System.err.println( "Erreur : " + e );
            }
        }
}
catch (Exception e)
{
System.err.println(e);
}
}



synchronized public void EnvoyerATous (String s)
{
for (int i=0; i<V.size(); i++)
{
ThreadClient c=(ThreadClient)V.elementAt(i);
c.Envoyer(s);
}
}



public void ajouterClient(ThreadClient c)
{
V.addElement(c);
}


synchronized public void EnvoyerListeClients (PrintWriter out)
{
//... ( envoyer dans out le nom de tous les clients du vecteur V)
for (int i=0; i<V.size(); i++)
{
ThreadClient c=(ThreadClient)V.elementAt(i);
out.println(c.nom+" est connecté \n");
}

}


synchronized public void SupprimerClient (ThreadClient c)
{
V.removeElement(c);

}}


class ThreadClient extends Thread
{
BufferedReader In;
PrintWriter Out;
ServeurIRC ser;
String nom="*****";

public ThreadClient (Socket socket, ServeurIRC s)
{

ser=s;
try{
Out = new PrintWriter(socket.getOutputStream(), true);
In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
}         catch (IOException e) {
            System.err.println("Erreur : " + e);}

}

public void run ()
{
	try{nom=In.readLine();
	ser.ajouterClient(this);
	ser.EnvoyerATous("L'utilisateur  "+nom+" s'est connecté.\n");
	ser.EnvoyerListeClients(Out);

	            while (true) {

                 String message = In.readLine();

                ser.EnvoyerATous(message);

            }
        }catch(IOException e) {}
}

public void Envoyer(String s) // Envoie vers le client
{
Out.println(s);
Out.flush();
}
public String nom() { return nom; }

}