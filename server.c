/******************************************
*    @author: Andrew Misicko, Truc Chau   *
*       server.c                          *
******************************************/  

#include<stdio.h>
#include<stdlib.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<unistd.h>
#include<pthread.h> 
#include <errno.h>
#include <string.h>
#define NUMBER_OF_CLIENT 10

//Send back the reversed string to client
void *ServerResponse(void *args)
{
  int incomingClientSocket=(int)args;
  char message[11];
    
  read(incomingClientSocket,message,11);
     
  //Reverse the input string
  //Declare a char array named "reversedString" to save the reversed string. 
  //i starts from 0 to 9 ; j starts from 9 to 0
  char reversedMessage[11];
  int i, j;
  for(i = 0, j=9; i < 10, j>=0; i++, j--) 
  {
    reversedMessage[i] = message[j];
  }
    
  write(incomingClientSocket,reversedMessage,11);
  printf("Sending message back to client...\n");
  close(incomingClientSocket);
  sleep(1);
}


int main()
{
  struct sockaddr_in sock_forServer;
  int serverSocket=socket(AF_INET,SOCK_STREAM,0); 
  pthread_t t[10];

  memset(&sock_forServer, '\0', sizeof(sock_forServer));
	
  /***Set up base socket***/
  sock_forServer.sin_port=htons(4446);
  sock_forServer.sin_family=AF_INET;
  sock_forServer.sin_addr.s_addr = INADDR_ANY; //INADDR_ANY = any incoming connection interface
     
  //Had an issue that the address is already used and that made the socket creation failed. Using setsockopt to fix it. 
  int option =1;
  setsockopt(serverSocket, SOL_SOCKET,SO_REUSEADDR, (char*)&option,sizeof(int));
  
  int currentIncomingSocket;
  int incomingSocket ;
  int i;

  //bind() should return non-negative value. That means binding is successful
  if(bind(serverSocket,(struct sockaddr*)&sock_forServer,sizeof(sock_forServer))>=0)
  {
    printf("**Socket created!**\n");
    
   listen(serverSocket,10);
    while(1)  
    {       
    for(i=0;i<NUMBER_OF_CLIENT;i++)     
    {                  // this waits for connection to come
      incomingSocket= accept(serverSocket,NULL,NULL);
      //Had an issue with the port number; used this intermediate variable currentIncomingSocket to fix the error "Creating socket failed"
      currentIncomingSocket = incomingSocket ; 
      printf("**Connected to client %d **\n", currentIncomingSocket);
      pthread_create(&t,NULL,ServerResponse,(void *) currentIncomingSocket);
    }
  }
    close(serverSocket);
  }
  else
  {
    printf("Creating socket failed\n");
  }   
  return 0;
}