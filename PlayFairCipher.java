/*COMPLETED. FINAL VIEW. REMOVED ALL BUGS. WORKS PERFECTLY FINE.*/

import java.util.Scanner;
import java.lang.String;
import java.io.Console;

public class PlayFairCipher //https://en.wikipedia.org/wiki/Playfair_cipher
{
  public static void main(String[] args)
  {
    Scanner sc = new Scanner(System.in);
    PlayFairCipher cipher = new PlayFairCipher();//object of this class
    String key = cipher.getKey();//this method has return value as String
    byte task = 0;//setting variable task to some random data
    System.out.print("Enter 1 to encrpyt and 0 to decrypt: ");//asking for task to user
    task = sc.nextByte();//taking a byte as input
    if(task==1)//getting plaintext and then printing the ciphertext
    {
      String plaintext = cipher.getPlainText();
      String encryptedText = cipher.Encryption(plaintext, key);
      System.out.println(encryptedText);
    }
    else//getting ciphertext and then printing plaintext
    {
      String ciphertext = cipher.getCipherText();
      String decryptedText = cipher.Decryption(ciphertext, key);
      System.out.println(decryptedText);
    }
  }
  public String getKey()//this method takes input for the key from user. Doesn't show text on screen so as to hide it from others.
  {
    java.io.Console console = System.console();//method of console class
    String key = new String(console.readPassword("Enter key: "));//converting password to String
    key = key.toUpperCase();//makes entire key upper case
    if(key.indexOf('Z')!=-1)//checks validation of key. if 'z' is found, getKey() method recursively gets called.
    {
      System.out.println("Character 'Z' found. Therefore, invalid key.");
      return getKey();
    }
    return key;
  }
  public String getPlainText()
  {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter plaintext: ");//asking user to input initial data
    String text = sc.nextLine();//the plaintext data is taken as input
    text = text.toUpperCase();//changing all characters to upper case
    if(text.indexOf('Z')!=-1)//if character 'z' is found, this method is called again. 'z' is absent in key matrix.
    {
      System.out.println("Character 'Z' found. Therefore, invalid statement.");
      return getPlainText();
    }
    return text;
  }
  public String getCipherText()
  {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter ciphertext: ");//asking user for cipher text
    String text = sc.nextLine();
    text =  text.trim();//removing extra spaces
    text = text.toUpperCase();//changing all characters to upper characters
    if(text.indexOf('Z')!=-1)//if character 'z' is found, this method is called again. 'z' is absent in key matrix.
    {
      System.out.println("Character 'Z' found. Therefore, invalid statement.");
      return getCipherText();
    }
    return text;
  }
  private String Encryption(String plainText, String key)
  {
    char[][] keyMatrix = makingMatrix(key);//matrix is made from different static method
    String modified_plainText = "";//this variable is used to make a new plaintext without any spaces
    String randomCharacter = "Q";//it is the random character that is used in case of a pair of same characters or when string length is odd
    for(int i=0; i<plainText.length(); i++)//this for loop makes a new plaintext without spaces and with 'Q'
    {
      if(plainText.charAt(i)>=65 && plainText.charAt(i)<=91)//checks whether character is an alphabet
      {
        if(i%2==1 && plainText.charAt(i)==plainText.charAt(i-1))//if it makes a pair of same characters, they are seperated with 'Q'
        {
          modified_plainText = modified_plainText.concat(randomCharacter);
        }
        else
        {
          modified_plainText = modified_plainText + plainText.charAt(i);
        }
      }
    }
    if(modified_plainText.length()%2==1)//if length is odd, add 'Q' at end
    {
      modified_plainText = modified_plainText.concat(randomCharacter);
    }
    String cryptedText = "";//initialised the string storing crypted data
    for(int i=0; i<modified_plainText.length()-1; i+=2)
    {
      int location1 = findCharInMatrix(modified_plainText.charAt(i), keyMatrix);
      int location2 = findCharInMatrix(modified_plainText.charAt(i+1), keyMatrix);
      if(location1/10==location2/10)//if both alphabets are in same row of keyMatrix
      {
        char crypt1 = nextCharInRow(location1, keyMatrix);
        char crypt2 = nextCharInRow(location2, keyMatrix);
        cryptedText = cryptedText+crypt1+crypt2;
      }
      else if(location1%10==location2%10)//if both alphabets are in same column of keyMatrix
      {
        char crypt1 = nextCharInColumn(location1, keyMatrix);
        char crypt2 = nextCharInColumn(location2, keyMatrix);
        cryptedText = cryptedText+crypt1+crypt2;
      }
      else//for remaining cases
      {
        char crypt1 = charAtIntersection(location1, location2, keyMatrix);
        char crypt2 = charAtIntersection(location2, location1, keyMatrix);
        cryptedText = cryptedText+crypt1+crypt2;
      }
    }
    return cryptedText;
  }
  private String Decryption(String cryptedText, String key)
  {
    char[][] keyMatrix = makingMatrix(key);//matrix is made from different static method
    String realText = "";
    for(int i=0; i<cryptedText.length()-1; i+=2)
    {
      int location1 = findCharInMatrix(cryptedText.charAt(i), keyMatrix);
      int location2 = findCharInMatrix(cryptedText.charAt(i+1), keyMatrix);
      if(location1/10==location2/10)
      {
        char decrypt1 = preCharInRow(location1, keyMatrix);
        char decrypt2 = preCharInRow(location2, keyMatrix);
        realText = realText + decrypt1 + decrypt2;
      }
      else if(location1%10==location2%10)
      {
        char decrypt1 = preCharInColumn(location1, keyMatrix);
        char decrypt2 = preCharInColumn(location2, keyMatrix);
        realText = realText + decrypt1 + decrypt2;
      }
      else
      {
        char decrypt1 = charAtIntersection(location1, location2, keyMatrix);
        char decrypt2 = charAtIntersection(location2, location1, keyMatrix);
        realText = realText + decrypt1 + decrypt2;
      }
    }
    if(realText.charAt(realText.length()-1)=='Q')
    {
      realText = realText.substring(0, realText.length()-1);
    }
    String originalText = "";
    for(int i=0; i<realText.length(); i++)
    {
      if(realText.charAt(i)!='Q')
      {
        originalText = originalText+realText.charAt(i);
      }
      else if(realText.charAt(i-1)!=realText.charAt(i+1))
      {
        originalText = originalText+realText.charAt(i);
      }
    }
    return originalText;
  }
  public static char[][] makingMatrix(String key)//this method makes the key matrix
  {
    char[][] matrix = new char[5][5];
    int length = 0;
    for(int i=0; i<key.length(); i++)
    {
      char currentCharacter = key.charAt(i);
      int firstIndex = key.indexOf(currentCharacter);
      if(firstIndex==i)
      {
        matrix[length/5][length%5] = currentCharacter;
        length++;
      }
    }
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXY";
    for(int i=0; i<25; i++)
    {
      if(key.indexOf(alphabet.charAt(i))==-1)
      {
        matrix[length/5][length%5] = alphabet.charAt(i);
        length++;
      }
    }
    return matrix;
  }
  public static int findCharInMatrix(char z, char[][] matrix)//this method is used to find particular character in the key matrix
  {
    int number = 0;
    for(int i=0; i<5; i++)
    {
      for(int j=0; j<5; j++)
      {
        if(matrix[i][j]==z)
        {
          number = i*10;
          number = number+j;
          break;
        }
      }
    }
    return number;
  }
  public static char nextCharInRow(int coordinates, char[][] matrix)//this method finds next character to a particular character in a row in matrix
  {
    int i = coordinates/10;
    int j = coordinates%10;
    if(j==4)
    {
      return matrix[i][0];
    }
    return matrix[i][j+1];
  }
  public static char nextCharInColumn(int coordinates, char[][] matrix)//this method finds next character to a particular character in a column of a matrix
  {
    int i = coordinates/10;
    int j = coordinates%10;
    if(i==4)
    {
      return matrix[0][j];
    }
    return matrix[i+1][j];
  }
  public static char charAtIntersection(int pos1, int pos2, char[][] matrix)
  {
    int i1 = pos1/10;
    int j1 = pos1%10;
    int i2 = pos2/10;
    int j2 = pos2%10;
    return matrix[i1][j2];
  }
  public static char preCharInRow(int coordinates, char[][] matrix)
  {
    int i = coordinates/10;
    int j = coordinates%10;
    if(j==0)
    {
        return matrix[i][4];
    }
    return matrix[i][j-1];
  }
  public static char preCharInColumn(int coordinates, char[][] matrix)
  {
    int i = coordinates/10;
    int j = coordinates%10;
    if(i==0)
    {
        return matrix[4][j];
    }
    return matrix[i-1][j];
  }
}
