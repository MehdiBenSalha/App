
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
class ClientFenetre extends Frame implements Runnable,ActionListener
{
String nom;
TextArea Output;
TextField Input;
Socket socket = null;
BufferedReader in ;
PrintWriter out ;
public ClientFenetre(InetAddress hote, int port, String n)
{
    super("Client en fenetre");
    nom=n;

// mise en forme de la fenetre (frame)
setSize(500,700);
setLayout(new BorderLayout());
add( Output=new TextArea(),BorderLayout.CENTER );
Output.setEditable(false);
add( Input=new TextField(), BorderLayout.SOUTH );
Input.addActionListener(this);
pack();
show();
Input.requestFocus();
// ajout d'un window adapter pour reagir si on ferme la fenetre
addWindowListener(new WindowAdapter ()
{ public void windowClosing (WindowEvent e)
{
setVisible(false); dispose(); System.exit(0);
}
}
);
        try {
            socket = new Socket(hote,port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
		} catch (IOException e) {
            e.printStackTrace();}

Thread t=new Thread(this);
t.start();
}
public void run ()
{
out.println(nom);

            while(true){
                try{String msg = in.readLine();
                Output.append(msg + "\n");
				} catch(IOException e) {}

            }

}
public void actionPerformed (ActionEvent e)
{
if (e.getSource()==Input)
{
String phrase=Input.getText();
out.println(nom+">>> "+phrase);
// efface la zone de saisie
Input.setText("");
}
}
protected void finalize()
{
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
}
public static void main(String[] args)
{
try{
InetAddress hote = InetAddress.getLocalHost();
int port = 1973; 
Socket socket = null;
       try {
            if( args.length>=1 ) hote = InetAddress.getByName(args[0]);
            else hote = InetAddress.getLocalHost();
            if( args.length==2 ) port = Integer.parseInt(args[1]) ;
        } catch (UnknownHostException e) {}

ClientFenetre chatwindow = new ClientFenetre(hote, port, args[0]);

}catch (UnknownHostException e) {}

}}