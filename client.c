/**********************************
 *  client.c
 *  Created on: Apr 4, 2017
 *  @Author: Truc Chau
 **********************************/

#include<stdio.h>
#include<stdlib.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<unistd.h>
#include<string.h>
#include <time.h>
#include<math.h>

#define TRUE 1
#define FALSE 0

// Methods
int invertBinary(char* byte, unsigned char* invertedBinary);
int checkChecksum(char* message);
unsigned char generateChecksum(unsigned char sumValue);
int generateMessage(char ID, char destination, char data1, char data2, char* message);

void printMessage(char a[]);

//Client sets up to connect to server and communicate in main method
int main(int argc, char *argv[])
{ 
      int dataContent =1;
      srand(time(NULL));
     
     //Each client sends out 10 message. We can increase this number if we want to. 
      while(dataContent!=10)
      {
           struct sockaddr_in sock;
           int serverSocket=socket(AF_INET,SOCK_STREAM,0);
           char message[6];
           char messageFromServer[6];
      
            memset(&sock, '\0', sizeof(sock));
            //memset(&message, '\0', sizeof(message));
            
            /***Set up base socket***/
            sock.sin_addr.s_addr=inet_addr("localhost");
            sock.sin_family=AF_INET;
            sock.sin_port= htons(4446); //port number
            //printf("socket? %d\n", serverSocket);
      
          //Should return a non-negative value -> success to create connection
          if(connect(serverSocket,(struct sockaddr*)&sock,sizeof(sock))>=0)
          { 
               printf("Connected to server #%d... \n",serverSocket);
               char clientID='1';
            
              //random destination
              int randomDest = (rand()%4)+1; // choose random number for dest from 0 to 3
              while(randomDest==1)
              {
                  randomDest = (rand()%4)+1;
              }
              printf("Random dest = %d\n", randomDest);
              
                int check;
             
                check = generateMessage(clientID, (char)randomDest, dataContent, dataContent, message);
                //printMessage(message);
                
                if(check==0)
                {
                    printf("Message is created\n");
                }
                
                sleep(2); //wait 2 seconds to send next message
                
                 //Send the message to server
                 if( write(serverSocket,message,strlen(message)) <0)
                 printf("ERROR writing to socket");
                 
                 
                //Read the msg from the router
                //Check if the received message is corrupted or not. If not, print it out.
                  read(serverSocket,messageFromServer,6);
                  if(checkChecksum(messageFromServer) ==1)
                  {
                      printf("***Message (client): %s***\n",messageFromServer);
                  }
                  
                  else
                  {
                      printf("Message is corrupted\n");
                  }
                  
                  close(serverSocket);
                  
            }
            
             else
             {
                  printf("Creating socket failed...\n");
             }
      
                dataContent++;
                
                
              
          } //end of while loop

      
  return 0;
}

/**
 * Print out the message
 * @author: Truc Chau
 */
void printMessage(char a[])
{
    int i;
    printf("Message is ");
    for(i =0; i<5; i++)
    {
          printf(" %c. ", a[i]);
    }

}

/**
* Validates the checksum of a message
* @author Jessica Schlesiger
**/
int checkChecksum(char* message) {
	unsigned char checksum = message[2]; // Checksum of the message
	unsigned char sumValue = message[0]+message[1]+message[3]+message[4]; // sum of the data in the message

  // ****** Used for debugging
	//printf("%s\n%d %d %d %d %d\nChecksum: %c, %d | Sumvalue: %c, %d\n",message, message[0], message[1], checksum, message[3], message[4], checksum, checksum, sumValue, sumValue);

 // Generates correct checksum
	unsigned char correctChecksum = generateChecksum(sumValue);

 // Checks if the checksum is correct
	if (checksum == correctChecksum)
	{
		return TRUE;
	}
	return FALSE;
}

/**
* Creates a checksum based on the sumValue of the message
* @author Jessica Schlesiger
**/
unsigned char generateChecksum(unsigned char sumValue) {
	unsigned char binary[8];
	static unsigned char byte[8];
	unsigned char i;
	for(i = 0; i < 8; i++) // converts sum to binary, is reversed
	{
		byte[i] = '0' + ((sumValue & (1 << i)) > 0);
	}
	for (i=0;i<8;i++) // fixes it being in reverse
	{
		binary[7-i]=byte[i];
	}

	unsigned char invertedBinary[8];
	invertBinary(binary, invertedBinary);

  // ****** Used for debugging
	//printf("Binary: \t%s\n", binary); // before invert
	//printf("Inverted:\t%s\n", invertedBinary); // is inverted

	// Converts the invertedBinary to an integer value
	unsigned char checksum=0;
	for (i=0;i<8;i++)
	{
		// Converts one piece at a time; 48 is the value of 0 in a char
		checksum=checksum+((pow(2, 7-i))*(int)(invertedBinary[i]-48));
	}
	return checksum;
}


/**
* Generates the compete message (including checksum) based on the given data
* @author Jessica Schlesiger
**/
int generateMessage(char ID, char destination, char data1, char data2, char* message)
{
	unsigned char sumValue = ID+destination+data1+data2; // What all the bytes add up to
	unsigned char checksum=generateChecksum(sumValue);

  // ****** Used for debugging
	//printf("%d, %d, %d, %d\nSumValue: %d\n",ID, destination, data1, data2, sumValue);
	//printf("%d, Checksum: %c\n", checksum, checksum); // working properly, but terminal will not display special characters

	// Compiles pieces
	message[4] = data2;
	message[0] = ID;
	message[1]= destination;
	message[2]= checksum;
	message[3]= data1;
	return 0;
}

/**
* Given a string of 8 bits, it inverts the 1's and 0's
* @author Jessica Schlesiger
**/
int invertBinary(char* byte, unsigned char* invertedBinary) {
	int i;
	for (i=0;i<8;i++) {

		if (byte[i] == '1') {
			invertedBinary[i] = '0';
		} else {
			invertedBinary[i]='1';
		}
	}
	return 0;
}
