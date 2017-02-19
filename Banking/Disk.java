import java.io.*;

 class ObjectOutputStream2 extends 
     ObjectOutputStream {

     public ObjectOutputStream2(OutputStream out) throws IOException {
      super(out);
    }

    protected void writeStreamHeader() throws IOException {
      reset();
    }
  }



class Person implements Serializable {
    public static final long serialVersionUID = 331000;
    
    public int i = 1;
    public Person() {
        
    }
}

public class Disk {

    public static Object load(String objFile) 
	throws IOException 
    {
	FileInputStream fis = new FileInputStream(objFile);
	ObjectInputStream ois = new ObjectInputStream(fis);
	Object obj = null;
	try {
	    obj = ois.readObject();
	} catch (ClassNotFoundException e) {
	    System.out.println("load failed: " + e);
	    System.exit(1);
	}
	ois.close();
	fis.close();
	return obj;
     }


    public static void save(Serializable obj, String objFile) 
	throws IOException
    {
	FileOutputStream fos = new FileOutputStream(objFile);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	oos.writeObject(obj);
	oos.close();
	fos.close();
    }

    public static void append(Serializable obj, String objFile) 
	throws IOException
    {
      
        File file = new File(objFile);
        boolean streamChoice = (file.exists());
        FileOutputStream fos = new FileOutputStream(objFile,true);
        ObjectOutputStream oos;        
                if (streamChoice)
                  oos = new ObjectOutputStream2(fos);
                else
                  oos = new ObjectOutputStream(fos);


        
        
        oos.writeObject(obj);
	oos.close();
	fos.close();
    }
    
}
