package tarea2_pdi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class JControlador {
    private final JInterfaz rolMenu;
    private final OpenFile of;
    private int Cantidad_grados = 0;
    private int pasos = 0;
    private int Tipo_imagen = 0;
    public BufferedImage actual;
    public BufferedImage rotada;
    public JControlador (){
       rolMenu = new JInterfaz (this);
       of = new OpenFile();
       rolMenu.setVisible(true);
    }
    public void SeleccionarOpcion (int a) throws IOException{
        int heigth, width, i, j, aux, aux_2, aux_width, aux_heigth, temp;
        BufferedImage picture_2;
        switch (a){
            case 1: //Abrir Archivo
            of.PickMe();
            if(of.chosen_picture != null){
                pasos = 0;
                actual = of.chosen_picture;
                rotada = of.chosen_picture;
                Cantidad_grados = 0;
                Tipo_imagen = 0;
                rolMenu.setValues(of.chosen_picture);
                rolMenu.setEneabled_B();
                rolMenu.colocar_datos(of.chosen_picture.getWidth(), of.chosen_picture.getHeight(), of.tamano, of.nombre);
                try {
                    SeleccionarOpcion(9);
                } catch (IOException ex) {
                    Logger.getLogger(JControlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;      
            case 2: //Guardar Archivo
                int pasos_nuevo = pasos;
                while (pasos_nuevo != 0){
                    if(pasos_nuevo > 0){
                       bilineal(actual.getWidth()/2,actual.getHeight()/2, 2);
                       pasos_nuevo--;
                    }else{
                       bilineal(actual.getWidth()*2,actual.getHeight()*2, 2);
                       pasos_nuevo++;
                    }
                }
                heigth = of.chosen_picture.getHeight();
                width = of.chosen_picture.getWidth();
                picture_2 = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
                picture_2 = of.chosen_picture;
                of.chosen_picture = actual;
                of.saveImage();
                of.chosen_picture = picture_2;
                pasos = 0;
            break;
            case 3: //Espejo Horizontal
                heigth = actual.getHeight();
                width = actual.getWidth();
                picture_2 = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
                aux = width - 1;
                for(i = 0; i < heigth; i++){
                    for(j = 0; j < width; j++){
                        temp = actual.getRGB(aux, i);
                        picture_2.setRGB(j, i, temp);
                        aux--;
                    }
                     aux = width - 1;
                }
                actual = picture_2;
                rolMenu.setValues(actual);
                if(Tipo_imagen == 2) rotacion(0, 2);
            break;
            case 4: //Espejo Vertical
                heigth = actual.getHeight();
                width = actual.getWidth();
                picture_2 = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
                aux = heigth - 1;
                for(i = 0; i < heigth; i++){
                    for(j = 0; j < width; j++){
                        temp = actual.getRGB(j, aux);
                        picture_2.setRGB(j, i, temp);
                    }
                    aux--;
                }
               
                actual = picture_2;
                rolMenu.setValues(actual);
                if(Tipo_imagen == 2) rotacion(0, 2);
            break;
            case 5: //Negativo
                int rgb;
                Color myWhite;
                heigth = actual.getHeight();
                width = actual.getWidth();
                myWhite = new Color(255, 255, 255);
                rgb = myWhite.getRGB();
                picture_2 = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
                for(i = 0; i < width; i++){
                    for(j = 0; j < heigth; j++){
                        aux = rgb - actual.getRGB(i, j);
                        picture_2.setRGB(i, j, aux);
                    }
                }
                actual = picture_2;
                rolMenu.setValues(actual);
                if(Tipo_imagen == 2) rotacion(0, 2);
                try {
                    SeleccionarOpcion(9);
                } catch (IOException ex) {
                    Logger.getLogger(JControlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            break;
            case 6:
                heigth = actual.getHeight();
                width = actual.getWidth();
                picture_2 = new BufferedImage(heigth, width, BufferedImage.TYPE_INT_RGB);
                aux_width = 0;
                aux_heigth = width - 1;
                for(i = 0; i < width; i++){
                    for(j = 0; j < heigth; j++){
                        temp = actual.getRGB(i, j);
                        picture_2.setRGB(aux_width, aux_heigth, temp);
                        aux_width++;
                    }
                    aux_heigth--;
                    aux_width = 0;
                }
                rotada = picture_2;
                rolMenu.setValues(rotada);
            break;
            case 9: //Histograma
                Histograma MyHistograma = new Histograma();
                int[][] histograma = MyHistograma.Crear_histograma(actual);
                for (i = 0; i < 4; i++) {
                    int[] histogramaCanal = new int[256];
                    System.arraycopy(histograma[i], 0, histogramaCanal, 0, histograma[i].length);
                    switch(i){
                        case 0:
                            rolMenu.Crear_Grafica(histogramaCanal, Color.red, 0, "Red");
                            break;
                        case 1:
                            rolMenu.Crear_Grafica(histogramaCanal, Color.green, 1, "Green");
                            break;
                        case 2:
                            rolMenu.Crear_Grafica(histogramaCanal, Color.blue, 2, "Blue");
                            break;
                        case 3:
                            rolMenu.Crear_Grafica(histogramaCanal, Color.gray, 3, "RGB");
                            break;
                    }
                }
            break;
        }
    }
    
    void brillo(int valor){
        int heigth, width, i, j, temp, r, g, b;
        BufferedImage picture_2;
        Color true_color;
        if(Tipo_imagen == 0) Tipo_imagen = 1;
        heigth = actual.getHeight();
        width = actual.getWidth();
        picture_2 = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        
        for(i = 0; i < width; i++){
            for(j = 0; j < heigth; j++){
                Color c = new Color(actual.getRGB(i, j),true);
                r = c.getRed() +  valor;
                g = c.getGreen() + valor;
                b = c.getBlue() + valor;
                if(r > 255) r = 255;
                
                if(g > 255) g = 255;
                                
                if(b > 255) b = 255;
                
                if(r < 0) r = 0;
                
                if(g < 0) g = 0;
                                
                if(b < 0) b = 0;
                
                true_color = new Color(r,g,b);
                temp = true_color.getRGB();
                picture_2.setRGB(i, j, temp);
            }
        }
        
        actual = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        actual = picture_2;
        rolMenu.setValues(actual);
        if(Tipo_imagen == 2) rotacion(0, 2);

        try {
            SeleccionarOpcion(9);
        } catch (IOException ex) {
            Logger.getLogger(JControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void umbralizacion(int valor){
        int heigth, width, i, j, temp, r, g, b;
        BufferedImage picture_2;
        Color true_color;
        if(Tipo_imagen == 0) Tipo_imagen = 1;
        heigth = actual.getHeight();
        width = actual.getWidth();
        picture_2 = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        
        if(valor < 0) valor = 0;
        
        for(i = 0; i < width; i++){
            for(j = 0; j < heigth; j++){
                Color c = new Color(actual.getRGB(i, j),true);
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();

                if(((r + g + b) /3)> valor){ r = 255; g = 255; b = 255; }else{
                    r = 0; g = 0; b = 0;
                }

                true_color = new Color(r,g,b);
                temp = true_color.getRGB();
                picture_2.setRGB(i, j, temp);
            }
        }
        
        actual = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        actual = picture_2;
        rolMenu.setValues(actual);
        if(Tipo_imagen == 2) rotacion(0, 2);
        
        try {
            SeleccionarOpcion(9);
        } catch (IOException ex) {
            Logger.getLogger(JControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void contraste(int valor){
        int heigth, width, i, j, temp, r, g, b;
        BufferedImage picture_2;
        float factor_contraste;
        Color true_color;
        if(Tipo_imagen == 0) Tipo_imagen = 1;
       
        heigth = actual.getHeight();
        width = actual.getWidth();
        picture_2 = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        factor_contraste = (float)(259*(valor + 255))/(float)(255*(259 - valor));
        
        for(i = 0; i < width; i++){
            for(j = 0; j < heigth; j++){
                Color c = new Color(actual.getRGB(i, j),true);
                r = (int) (factor_contraste*(c.getRed() - 128) + 128);
                g = (int) (factor_contraste*(c.getGreen() - 128) + 128);
                b = (int) (factor_contraste*(c.getBlue() - 128) + 128);

                if(r > 255) r = 255;
                
                if(g > 255) g = 255;

                if(b > 255) b = 255;

                if(r < 0) r = 0;

                if(g < 0) g = 0;

                if(b < 0) b = 0;

                true_color = new Color(r,g,b);
                temp = true_color.getRGB();
                picture_2.setRGB(i, j, temp);
            }
        }
        
        actual = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        actual = picture_2;
        rotada = actual;
        rolMenu.setValues(actual);
        if(Tipo_imagen == 2) rotacion(0, 2);
      
        try {
            SeleccionarOpcion(9);
        } catch (IOException ex) {
            Logger.getLogger(JControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void ecualizar(){
        int heigth, width, i, j, histograma[][], histogramaR[], histogramaG[], histogramaB[], red, green, blue, newPixel;
        int histogramaR_reemplazo[], histogramaG_reemplazo[], histogramaB_reemplazo[], acum_r, acum_g, acum_b;
        float factor;
        BufferedImage histogramEQ;
        Histograma MyHistograma;
        if(Tipo_imagen == 0) Tipo_imagen = 1;
        heigth = actual.getHeight();
        width = actual.getWidth();
        
        MyHistograma = new Histograma();
        histograma = MyHistograma.Crear_histograma(actual);
        
        histogramaR = new int[256];
        histogramaG = new int[256];
        histogramaB = new int[256];
        
        histogramaR_reemplazo = new int[256];
        histogramaG_reemplazo = new int[256];
        histogramaB_reemplazo = new int[256];
        
        System.arraycopy(histograma[0], 0, histogramaR, 0, histograma[0].length);
        System.arraycopy(histograma[1], 0, histogramaG, 0, histograma[1].length);
        System.arraycopy(histograma[2], 0, histogramaB, 0, histograma[2].length);
        
        for(i=0; i<256; i++) histogramaR_reemplazo[i] = 0;
        for(i=0; i<256; i++) histogramaG_reemplazo[i] = 0;
        for(i=0; i<256; i++) histogramaB_reemplazo[i] = 0;
        
        factor = (float) ((255.0)/(width*heigth));
        acum_r = 0; acum_g = 0; acum_b = 0;
        
        for(i = 0; i < 256; i++){
            acum_r += histogramaR[i];
            histogramaR_reemplazo[i] = (int) (acum_r * factor);
            
            if(histogramaR_reemplazo[i] > 255) histogramaR_reemplazo[i] = 255;
            
            acum_g += histogramaG[i];
            histogramaG_reemplazo[i] = (int) (acum_g * factor);
            
            if(histogramaG_reemplazo[i] > 255) histogramaG_reemplazo[i] = 255;
           
            acum_b += histogramaB[i];
            histogramaB_reemplazo[i] = (int) (acum_b * factor);
            
            if(histogramaB_reemplazo[i] > 255) histogramaB_reemplazo[i] = 255;
        }
        
        //Asignacion a la imagen
        histogramEQ = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        
        for(i = 0; i < width; i++) {
            for(j = 0; j < heigth; j++) {
                red = new Color(actual.getRGB (i, j)).getRed();
                green = new Color(actual.getRGB (i, j)).getGreen();
                blue = new Color(actual.getRGB (i, j)).getBlue();
 
                red = histogramaR_reemplazo[red];
                green = histogramaG_reemplazo[green];
                blue = histogramaB_reemplazo[blue];
                
                Color c = new Color(red, green, blue);
                newPixel = c.getRGB();
                histogramEQ.setRGB(i, j, newPixel);
            }
        }
 
        rolMenu.setValues(histogramEQ);
        actual = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        actual = histogramEQ;
        if(Tipo_imagen == 2) rotacion(0, 2);
        try {
            SeleccionarOpcion(9);
        } catch (IOException ex) {
            Logger.getLogger(JControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
         
    void bilineal (int ancho_nuevo, int alto_nuevo, int tipo)
    {
        BufferedImage picture_2;
        int h, w;
        if(tipo == 3){
            h = of.chosen_picture.getHeight(); w = of.chosen_picture.getWidth();
        }else{
            h = actual.getHeight(); w = actual.getWidth();
        }
        if((alto_nuevo>60 || ancho_nuevo>60)&&(alto_nuevo<5698 || ancho_nuevo<5698)){
            if(tipo == 1){
                pasos++;
            }
            if(tipo == 2){
                pasos--;
            }
            if(tipo == 3){
                pasos = 0;
            }
            int[] pixels = new int[h*w];
            int aux_i = 0;
            for( int i = 0; i < h; i++ ){
                    for( int j = 0; j < w; j++ ){
                        if(tipo == 3) pixels[aux_i] = of.chosen_picture.getRGB( j, i );
                        else pixels[aux_i] = actual.getRGB( j, i );
                        aux_i++;
                    }
            }
            int[] temp = new int[ancho_nuevo*alto_nuevo] ;
            int a, b, c, d, x, y, index ;
            float x_ratio = ((float)(w-1))/ancho_nuevo ;
            float y_ratio = ((float)(h-1))/alto_nuevo ;
            float x_diff, y_diff, blue, red, green ;
            int offset = 0 ;
            for (int i=0;i<alto_nuevo;i++) {
                for (int j=0;j<ancho_nuevo;j++) {
                    x = (int)(x_ratio * j) ;
                    y = (int)(y_ratio * i) ;
                    x_diff = (x_ratio * j) - x ;
                    y_diff = (y_ratio * i) - y ;
                    index = (y*w+x) ;                
                    a = pixels[index] ;
                    b = pixels[index+1] ;
                    c = pixels[index+w] ;
                    d = pixels[index+w+1] ;

                    blue = (a&0xff)*(1-x_diff)*(1-y_diff) + (b&0xff)*(x_diff)*(1-y_diff) +
                           (c&0xff)*(y_diff)*(1-x_diff)   + (d&0xff)*(x_diff*y_diff);

                    green = ((a>>8)&0xff)*(1-x_diff)*(1-y_diff) + ((b>>8)&0xff)*(x_diff)*(1-y_diff) +
                            ((c>>8)&0xff)*(y_diff)*(1-x_diff)   + ((d>>8)&0xff)*(x_diff*y_diff);

                    red = ((a>>16)&0xff)*(1-x_diff)*(1-y_diff) + ((b>>16)&0xff)*(x_diff)*(1-y_diff) +
                          ((c>>16)&0xff)*(y_diff)*(1-x_diff)   + ((d>>16)&0xff)*(x_diff*y_diff);

                    temp[offset++] = 
                            0xff000000 | 
                            ((((int)red)<<16)&0xff0000) |
                            ((((int)green)<<8)&0xff00) |
                            ((int)blue) ;
                }
            }
            picture_2 = new BufferedImage(ancho_nuevo, alto_nuevo, BufferedImage.TYPE_INT_RGB);
            aux_i = 0;
            for( int i = 0; i < alto_nuevo; i++ ){
                    for( int j = 0; j < ancho_nuevo; j++ ){
                        picture_2.setRGB(j, i, temp[aux_i]);
                        aux_i++;
                    }
            }
            actual = new BufferedImage(ancho_nuevo, alto_nuevo, BufferedImage.TYPE_INT_RGB);
            actual = picture_2 ;
            if(tipo == 3){
                of.chosen_picture = picture_2;
                rolMenu.colocar_datos(of.chosen_picture.getWidth(), of.chosen_picture.getHeight(), of.tamano, of.nombre);
            }
            rolMenu.setValues(actual);
            if(Tipo_imagen == 2) rotacion(0, 2);
        }
    }
    
    void nearest(int ancho_nuevo,int alto_nuevo, int tipo) {
        BufferedImage picture_2;
        int h, w;
        if(tipo == 3){
            h = of.chosen_picture.getHeight(); w = of.chosen_picture.getWidth();
        }else{
            h = actual.getHeight(); w = actual.getWidth();
        }
        
        if((alto_nuevo>60 || ancho_nuevo>60)&&(alto_nuevo<5698 || ancho_nuevo<5698)){
            if(tipo == 1){
                pasos++;
            }
            if(tipo == 2){
                pasos--;
            }
            if(tipo == 3){
                pasos = 0;
            }
            int[] pixels = new int[h*w];
            int aux_i = 0;
            for( int i = 0; i < h; i++ ){
                    for( int j = 0; j < w; j++ ){
                        if(tipo == 3) pixels[aux_i] = of.chosen_picture.getRGB( j, i );
                        else pixels[aux_i] = actual.getRGB( j, i );
                        aux_i++;
                    }
            }
            int[] temp = new int[ancho_nuevo*alto_nuevo] ;
            int x_ratio = (int)((w<<16)/ancho_nuevo) +1;
            int y_ratio = (int)((h<<16)/alto_nuevo) +1;
            int x2, y2 ;
            for (int i=0;i<alto_nuevo;i++) {
                for (int j=0;j<ancho_nuevo;j++) {
                    x2 = ((j*x_ratio)>>16) ;
                    y2 = ((i*y_ratio)>>16) ;
                    temp[(i*ancho_nuevo)+j] = pixels[(y2*w)+x2] ;
                }                
            }                
            picture_2 = new BufferedImage(ancho_nuevo, alto_nuevo, BufferedImage.TYPE_INT_RGB);
            aux_i = 0;
            for( int i = 0; i < alto_nuevo; i++ ){
                    for( int j = 0; j < ancho_nuevo; j++ ){
                        picture_2.setRGB(j, i, temp[aux_i]);
                        aux_i++;
                    }
            }
            actual = new BufferedImage(ancho_nuevo, alto_nuevo, BufferedImage.TYPE_INT_RGB);
            actual = picture_2;
            rotada = picture_2;
            if(tipo == 3){
                of.chosen_picture = picture_2;
                rolMenu.colocar_datos(of.chosen_picture.getWidth(), of.chosen_picture.getHeight(), of.tamano, of.nombre);
            }
            rolMenu.setValues(actual);
            if(Tipo_imagen == 2) rotacion(0, 2);
          }
    }
    
    void rotacion(int angulo, int tipo){
        if(tipo == 1)
            Tipo_imagen = 2;
        
        Cantidad_grados+=angulo;
        if(Cantidad_grados > 360){
            Cantidad_grados-=360;
            rotada = actual;
        }
        if(Cantidad_grados < 90){
            rotada = actual;
        }
        
        angulo = Cantidad_grados;
        
        int cantidad = 0;
        
        while(angulo >= 90){
            cantidad +=1;
            angulo -= 90;
        }
        
        int casos = 0;
        while(cantidad != 0){
            if (casos != 0) {
                int heigth, width, i, j, aux_width, aux_heigth, temp;
                BufferedImage picture_2;
                heigth = rotada.getHeight();
                width = rotada.getWidth();
                picture_2 = new BufferedImage(heigth, width, BufferedImage.TYPE_INT_RGB);
                aux_width = 0;
                aux_heigth = width - 1;
                for(i = 0; i < width; i++){
                    for(j = 0; j < heigth; j++){
                        temp = rotada.getRGB(i, j);
                        picture_2.setRGB(aux_width, aux_heigth, temp);
                        aux_width++;
                    }
                    aux_heigth--;
                    aux_width = 0;
                }
                
                rotada = picture_2;
                rolMenu.setValues(rotada);
                
            }else{
            try {
                SeleccionarOpcion(6);
            } catch (IOException ex) {
                Logger.getLogger(JControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            casos ++;

            }
            cantidad -=1;
        }
        double sen = Math.sin(Math.toRadians(angulo));
        double tan = Math.tan(Math.toRadians(angulo/2));
        
        int i, j, x2 = 0, y2 = 0, temp;
        int h = rotada.getHeight(); int w = rotada.getWidth();
        int dist = (int)(Math.ceil(Math.sqrt((h*h)+(w*w))));

        
        BufferedImage picture_2;
      
        picture_2 = new BufferedImage(dist, dist, BufferedImage.TYPE_INT_RGB);
        Color blanco = new Color(238, 238, 238);
        int rgb = blanco.getRGB();
        for(i = 0; i < dist; i++){
            for(j = 0; j < dist; j++){
                picture_2.setRGB(i, j, rgb);
            }
        }
        int x0= (int)(Math.floor((w)/2));
        int y0= (int)(Math.floor((h)/2));
        for(i = 0; i < w; i++){
                for(j = 0; j < h; j++){ 
                    temp = rotada.getRGB(i, j);
                    
                    x2 = (int)((i-x0) - tan*(y0-j));
                    y2 = (int)(y0-j);
                    
                    y2 = (int)((sen*x2) + y2);
                    
                    x2 = (int)((x2) - tan*(y2))+((dist/2));
                    y2 = (int)(-y2)+((dist/2));

                    if(x2 < dist && y2 < dist && x2 > -1 && y2 > -1) picture_2.setRGB(x2, y2, temp);
                    x2 = 0;
                    y2 = 0;
                }
                
        }
        rotada = new BufferedImage(dist, dist, BufferedImage.TYPE_INT_RGB);
        rotada = picture_2;
        rolMenu.setValues(rotada);
    }
 
    void undo(){
        actual = of.chosen_picture;
        rolMenu.setValues(actual);
        Cantidad_grados = 0;
        Tipo_imagen = 1;
        try {
            SeleccionarOpcion(9);
        } catch (IOException ex) {
            Logger.getLogger(JControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 

}

