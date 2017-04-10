#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <pthread.h>
#include <math.h>

#define TRUE 1
#define FALSE 0

/**
* A router that will receive messages from clients/routers and then forward
* them to their proper destinations.
* Author: Brad Olah
*/

void* handleConnection(void *arg);
int invertBinary(char* byte, unsigned char* invertedBinary);
int checkChecksum(char* message);
unsigned char generateChecksum(unsigned char sumValue);


int main(int argc, char *argv[])
{
	int listeningSocket = 0;
	int connectingSocket = 0;
	int portNumber = 4446;

	struct sockaddr_in serverInfo;
	struct sockaddr_in clientInfo;
	
	if((listeningSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)
	{
		printf("Router: Socket Failed");
	}


	memset(&serverInfo, '0', sizeof(serverInfo));

	serverInfo.sin_family = AF_INET;
	serverInfo.sin_addr.s_addr = htonl(INADDR_ANY);
	serverInfo.sin_port = htons(portNumber);
	
	bind(listeningSocket, (struct sockaddr*)&serverInfo, sizeof(serverInfo));

	listen(listeningSocket,10);
	
	/*
	* Waits for a client/router to try to connect.
	* Makes a socket connection then creates a thread to
	* handle the client/routers message.
	*/
	while(1)
	{
		int clientLen = sizeof(clientInfo);
		connectingSocket = accept(listeningSocket, (struct sockaddr*)&clientInfo,(socklen_t*)&clientLen);
		printf("received connection request");
		char readBuffer[6];
		memset(readBuffer,'\0',sizeof(readBuffer));
		read(connectingSocket,readBuffer,sizeof(readBuffer));
		
		pthread_t thread;
		pthread_create(&thread,NULL,handleConnection,(void *)readBuffer);
	}

}

/*
* A thread that will forward the message on to either its destination,
* or the next step on the way to its destination.
*/
void* handleConnection(void *arg)
{
	puts("Thread Made");
  
	//Creates two character arrays to hold incomming and outgoing messages.
	char readBuffer[6];
	strcpy(readBuffer,arg);
	char sendBuffer[6];

	//Sets the memory of the character arrays to null terminators.
	//memset(readBuffer,'\0',sizeof(readBuffer));
	memset(sendBuffer,'\0',sizeof(sendBuffer));
		
	//for(int i = 0; i < 10; i++)
	{	
		int portNumber;
		//Reads in the send characer array and stores it.
		//read(inSocket,readBuffer,sizeof(readBuffer));
		
		char source = readBuffer[0];
		char destination = readBuffer[1];
		char checksum = readBuffer[2];
		char data_1 = readBuffer[3];
		char data_2 = readBuffer[4];
		
		printf("Recieved a message from %c\n",source); 
		printf("The destination for the message is %c\n",destination);

		printf("Checking the checksum...\n");
		//Checksum validation still needed.
		if(checkChecksum(readBuffer))
		{	
			printf("Checksum 'valid' (currently unchecked).\n");
			int destInt = destination - '0'; //The overall target as an integer. Used to find where to send to from the routing table.
			char dest[12];			 //The router to send to.
			char destinationAddress[12];	 //The address of the router to send to.

			//Looks up how to get the destination from the routing table.
			FILE *fp;
			fp = fopen("routingTable.txt","r");
			for(int j =0;j<destInt;j++)
			{
				fgets(dest,12,(FILE*)fp);
			}
			fclose(fp);	
			printf("To get to %c send to destination %s",destination,dest);
			
			//Looks up the address of the destination to send to if it is not localhost.
			if(strcmp(dest,"localhost"))
			{	
				printf("Sending to localhost\n");
				strcpy(destinationAddress,"localhost");
				portNumber = 4446;
			}else
			{
				int destAddrInt = dest-'0'; //The integer value for the address to send to. Used to get the proper line from the file.
				fp = fopen("serverAddresses.txt","r");
				for(int j = 0;j<destAddrInt;j++)
				{
					fgets(destinationAddress,12,(FILE*)fp);
				}
				fclose(fp);
				printf("The address for destination %i is %s\n",destAddrInt,destinationAddress);
				portNumber = 3336;
			}
						
		
			int routerSocket;
			struct sockaddr_in routerInfo;
			memset(&routerInfo,'\0',sizeof(routerInfo));

			routerSocket = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP);
			
			routerInfo.sin_family = AF_INET;
			routerInfo.sin_addr.s_addr = inet_addr(destinationAddress);
			routerInfo.sin_port = htons(portNumber);
			printf("Attempting to connect to destination %i",destInt);
			if(connect(routerSocket,(struct sockaddr*)&routerInfo,sizeof(routerInfo))>0)
			{
				printf("Router: connection failed with %i",destInt);
				pthread_exit(NULL);
			}
			printf("Sending the message to %i",destInt);
			write(routerSocket,readBuffer,sizeof(readBuffer));
			close(routerSocket);
		}
		else
		{
			printf("Checksum failed. Closing Thread.");
			pthread_exit(NULL);
		
				
		}
	}
	puts("Thread Closing");
	pthread_exit(NULL);
	
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
unsigned char generateChecksum(unsigned char sumValue) 
{
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
		checksum=checksum+(pow(2, 7-i)*(int)(invertedBinary[i]-48));	
	}
	return checksum;
}

/**
* Given a string of 8 bits, it inverts the 1's and 0's
* @author Jessica Schlesiger
**/
int invertBinary(char* byte, unsigned char* invertedBinary) 
{
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

