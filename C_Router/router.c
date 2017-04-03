#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <pthread.h>

/**
* A router that will receive messages from clients/routers and then forward
* them to their proper destinations.
* Author: Brad Olah
*/

void* handleConnection(void *arg);
int validateChecksum(char sum);

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
		
		pthread_t thread;
		pthread_create(&thread,NULL,handleConnection,(void *)connectingSocket);
	}

}

/*
* A thread that will forward the message on to either its destination,
* or the next step on the way to its destination.
*/
void* handleConnection(void *arg)
{
	//Stores the socket that this thread communicates on.
	int inSocket = (int)arg;
	puts("Thread Made");
  
	//Creates two character arrays to hold incomming and outgoing messages.
	char readBuffer[6];
	char sendBuffer[6];

	//Sets the memory of the character arrays to null terminators.
	memset(readBuffer,'\0',sizeof(readBuffer));
	memset(sendBuffer,'\0',sizeof(sendBuffer));
		
	//for(int i = 0; i < 10; i++)
	{	
		int portNumber;
		//Reads in the send characer array and stores it.
		read(inSocket,readBuffer,sizeof(readBuffer));
		
		char source = readBuffer[0];
		char destination = readBuffer[1];
		char checksum = readBuffer[2];
		char data_1 = readBuffer[3];
		char data_2 = readBuffer[4];
		
		printf("Recieved a message from %c\n",source); 
		printf("The destination for the message is %c\n",destination);

		printf("Checking the checksum...\n");
		//Checksum validation still needed.
		if(validateChecksum(checksum))
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
		}
		else
		{
			printf("Checksum failed. Closing Thread.");
			pthread_exit(NULL);
				
		}
	}
	puts("Thread Closing");
	pthread_exit(NULL);
	close(socket);
}

int validateChecksum(char sum)
{
	
	return 1;
}
