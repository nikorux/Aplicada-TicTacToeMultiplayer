

 //Contiene solo el botón salir

 import javax.swing.*;
 import java.awt.*;

 public class ButtonPanel extends JPanel {
     public ButtonPanel(NoughtsCrossesModel model, JFrame frame) {
         super();
         setBackground(new Color(255, 239, 213));
         JButton exit = new JButton("Salir del Juego");
         exit.setBackground(new Color(238, 220, 220));
         exit.addActionListener(e -> { //Cuando se presione el boton de salir de la ventana
             int confirmed = JOptionPane.showConfirmDialog(frame,
                     "¿Estas seguro de que quieres rendirte? ¡Perderas el juego!", "Confirma tu eleccion.",
                     JOptionPane.YES_NO_OPTION);

             if (confirmed == JOptionPane.YES_OPTION) {
                 NoughtsCrossesGUI.forfeit();

             }
         });

         add(exit);
     }
 }
