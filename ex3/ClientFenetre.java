
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
class ClientFenetre extends Frame implements Runnable,ActionListener
{
TextArea Output;
TextField Input;
Socket soc = null;
BufferedReader in ;
PrintWriter out ;
public ClientFenetre(InetAddress hote, int port)
{
super("Client en fenetre");
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
            
            soc = new Socket(hote,port);
            out = new PrintWriter(soc.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(soc.getInputStream())); 
		} catch (IOException e) {
            e.printStackTrace();}

Thread th=new Thread(this);
th.start();
}
public void run ()
{


           int i =0;
            while(i<10){
                try{String message = in.readLine();
                Output.append(message + "\n");
                i++;
				} catch(IOException e) {}

            }

}
public void actionPerformed (ActionEvent e)
{
if (e.getSource()==Input)
{
String s=Input.getText();
out.println(s);
// efface la zone de saisie
Input.setText("");
}
}
protected void finalize()
{


            try {
                soc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


}
public static void main(String[] args)
{
InetAddress hote = null;
int port = 1973; // par d faut
Socket soc = null;


        try {
            if( args.length>=1 ) hote = InetAddress.getByName(args[0]);
            else hote = InetAddress.getLocalHost();
            if( args.length==2 ) port = Integer.parseInt(args[1]) ;
        } catch (UnknownHostException e) {}



ClientFenetre chatwindow = new ClientFenetre(hote, port);
}}