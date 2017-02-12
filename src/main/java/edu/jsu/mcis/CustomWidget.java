package edu.jsu.mcis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class CustomWidget extends JPanel implements MouseListener {
    private java.util.List<ShapeObserver> observers;
    
    
    private final Color HEX_SELECTED_COLOR = Color.green;
    private final Color OCT_SELECTED_COLOR = Color.red;
    private final Color DEFAULT_COLOR = Color.white;
    private boolean selected;
    private Point[] hexVertex;
    private Point[] octVertex; 
    
    
    
    
    public CustomWidget() {
        observers = new ArrayList<>();
        Dimension dim = getPreferredSize();
        selected = false;
        hexVertex = new Point[6];
        
        for(int i = 0; i < hexVertex.length; i++) 
        { 
            hexVertex[i] = new Point(); 
        }
         octVertex = new Point[8];
        for(int i = 0; i < octVertex.length; i++) 
        { 
            octVertex[i] = new Point();
        }
              
        calculateVertices(dim.width, dim.height);
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(this);
    }

    
    public void addShapeObserver(ShapeObserver observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }
    public void removeShapeObserver(ShapeObserver observer) {
        observers.remove(observer);
    }
    private void notifyObservers() {
        ShapeEvent event = new ShapeEvent(selected);
        for(ShapeObserver obs : observers) {
            obs.shapeChanged(event);
        }
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    private void calculateVertices(int width, int height) {
       int size = Math.min(width, height) / 2;
        
        for(int i = 0; i < hexVertex.length; i++) {
            
            double rad = 0 + (i *(Math.PI /(hexVertex.length /2 )));
            double x = Math.cos(rad);
            double y = Math.sin(rad);
            hexVertex[i].setLocation(width/3 + (x * (size/4)), height/2 + (y*(size/4)));
            
        }
        for(int i = 0; i < octVertex.length; i++) 
        {
            
            double rad = Math.PI * 0.125 + (i * (Math.PI / (octVertex.length / 2)));
            double x = Math.cos(rad);
            double y = Math.sin(rad);
            octVertex[i].setLocation(width - (width/3) + (x * (size/4)), height/2 + (y *(size/4)));
            
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        calculateVertices(getWidth(), getHeight());
        Shape  shape [] = getShapes();
        g2d.setColor(Color.black);
        g2d.draw (shape[0]);
        g2d.draw (shape[1]);

        if(selected == true) 
        {
            g2d.setColor(HEX_SELECTED_COLOR);
            g2d.fill(shape[0]);
            g2d.setColor(DEFAULT_COLOR);
            g2d.fill(shape[1]);
        }

        else if (selected == false)
        {
            g2d.setColor(DEFAULT_COLOR);
            g2d.fill(shape[0]);
            g2d.setColor(OCT_SELECTED_COLOR);
            g2d.fill(shape[1]);
        }
    }
    @Override
    public void mouseClicked(MouseEvent event) {
        Shape shape [] = getShapes();
        
        if(shape[0].contains(event.getX(), event.getY())) 
        {
            selected = true;
            notifyObservers();
        }    
        if(shape[1].contains(event.getX(), event.getY()))
        {
            selected = false;
            notifyObservers();
        }
        repaint(shape[0].getBounds());
        repaint(shape[1].getBounds());
    }
    
    public void mousePressed(MouseEvent event) {}
    public void mouseReleased(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
    
    public Shape[] getShapes() {
        Shape shape[] = new Shape[2];
        int[] x = new int[hexVertex.length];
        int[] y = new int[hexVertex.length];
        
        for(int i = 0; i < hexVertex.length; i++) 
        {
            x[i] = hexVertex[i].x;
            y[i] = hexVertex[i].y;
        }
        shape[0] = new Polygon(x, y, hexVertex.length);
        
            x = new int[octVertex.length];
            y = new int[octVertex.length];
        
         for(int i = 0; i < octVertex.length; i++) 
         {
            x[i] = octVertex[i].x;
            y[i] = octVertex[i].y;
         }
        shape[1] = new Polygon(x, y, octVertex.length);
        
        return shape;
    }
    public boolean isSelected() { return selected; }



	public static void main(String[] args) {
		JFrame window = new JFrame("Custom Widget");
        window.add(new CustomWidget());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(300, 300);
        window.setVisible(true);
	}
}
