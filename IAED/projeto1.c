#include <stdio.h>
#include <string.h>
/* The maximum size of the description of a task */
#define tafdesc 52
/* The maximum size of each user name */
#define usersize 22
/* The maximum number of tasks */
#define Maxtaf 10000
/* The maximum number of activities */
#define Maxativ 10
/* The maximum size of each activity name*/
#define ativsize 22
/* The maximum number of users */
#define Maxuser 50

/* struct represented by four ints( ativ - number that represents an activity,
 * prev - previewed duration of the task, start - instant in which the task
 * leaves the activity TO DO, user - number that represents an user)
 * and a string that represents the task's description*/
typedef struct tarefa{
	int ativ;
	int prev;
	int start;
	char desc[tafdesc];
	int user;
}tarefa;

/* Auxiliary function that fills a task when its created */
void filltask(struct tarefa task[],int d, char des[], int n){
	task[n].ativ = 0;
	task[n].prev = d;
	strcpy(task[n].desc, des);
	task[n].start = 0;
}

/* This function creates a taskand prints its id (number of tasks plus one)
 * or prints an error if the there are too many tasks, if the task has an invalid
 * duration or if already exists a task with the inputed description*/
int novatarefa(struct tarefa task[], int numtarefa){
	int duracao, i, error = 0;
	char descricao[tafdesc];
	if(numtarefa == Maxtaf) printf("too many tasks\n");

	else{
		scanf("%d ", &duracao);
		fgets(descricao, tafdesc, stdin);
		descricao[strlen(descricao) - 1] = '\0';

		for(i = 0; i < numtarefa; i++){
			if(strcmp(task[i].desc, descricao) == 0){
				printf("duplicate description\n");
				error = 1;
				break;
			}
		}
		if(duracao <= 0 && error == 0) printf("invalid duration\n");
		
		else if(error == 0){
			filltask(task, duracao, descricao,numtarefa);
			printf("task %d\n", ++numtarefa);
		}
	}
	return numtarefa;
}

/* This function adds to the variable time the number inputed
 * and prints the new time or prints an error if the inputed
 * number is negative
 * */
int passartempo(int tempo){
	int pass;
	scanf("%d", &pass);
	if(pass >= 0){
		tempo = tempo +pass;
		printf("%d\n", tempo);
	}
	else printf("invalid time\n");

	return tempo;
}

void alphabeticsort(struct tarefa task[],int numtarefa,char atividade[][ativsize]);

void idprint(int list[], int n,struct tarefa task[], int num,char ativ[][ativsize]);


/* This function determines if all tasks are alphabetic sorted or if the inputed
 * tasks are printed in the order that they are inputed, according to the input*/
void listtarefa(struct tarefa task[], int numtarefa, char atividade[][ativsize]){
	char c;
	int list[Maxtaf], i = 0, id;
	while((c = getchar()) != '\n'){
		scanf("%d", &id);
		list[i] = id;
		++i;
	}
	if(i == 0) alphabeticsort(task, numtarefa, atividade);

	else idprint(list, i,task ,numtarefa , atividade);
}

/* This function takes the last element as a pivot and places all
 * alphabetic smaller elements on its left  and the others on is right
 * doing the same movements on the id array*/
int alphabeticpart(char sort[][tafdesc],int id[], int min, int max){
	char pivo[tafdesc], t[tafdesc];
	int i = min - 1, j, tr;
	strcpy(pivo, sort[max]);
	for(j = min; j < max; j++){
		if(strcmp(sort[j], pivo) < 0){
			++i;
			strcpy(t, sort[i]);
			tr = id[i];
			strcpy(sort[i], sort[j]);
			id[i] = id[j];
			strcpy(sort[j], t);
			id[j] = tr;
		}
	}
	strcpy(t, sort[i+1]);
	tr = id[i +1];
	strcpy(sort[i+1], sort[max]);
	id[i+1] = id[max];
	strcpy(sort[max], t);
	id[max] = tr;
	return(i+1);
}

/* This function takes the last element as a pivot and places all
 * smaller elements on its left  and the others on is right
 * doing the same movements on the id array
 * */
int numericpart(int sort[], int id[], int min, int max){
	int t, tr,j, pivo = sort[max], i = min -1;
	for(j = min; j< max; j++){
		if(sort[j] < pivo){
			i++;
			t = sort[i];
			tr = id[i];
			sort[i] = sort[j];
			id[i] = id[j];
			sort[j] = t;
			id[j] = tr;
		}
	}
	t = sort[i +1];
	tr = id[i +1];
	sort[i+1] = sort[max];
	id[i +1] = id[max];
	sort[max] = t;
	id[max] = tr;
	return(i +1);
}


/* Alphabetic quicksort that receives an array of strings and another of ids
 * and sorts them in alphabetic order with help of an auxiliary function
 * */
void aquicksort(char sort[][tafdesc],int id[], int min, int max){
	int part;
	if(min < max){
		part = alphabeticpart(sort,id, min, max);

		aquicksort(sort,id, min, part -1);
		aquicksort(sort,id, part +1, max);
	}
}

/* Numeric quicksort that receives an array of numbers and another 
 * of ids and sorts them in numeric order with help of an auxiliary function
 * */

void nquicksort(int sort[], int id[], int min, int max){
	int part;
	if(min < max){
		part = numericpart(sort, id, min, max);

		nquicksort(sort, id, min, part -1);
		nquicksort(sort, id , part +1, max);
	}
}

/* Puts all task's descriptions in a array and all ids in another and
 * sends them to an alphabetic quicksort
 * */

void alphabeticsort(struct tarefa task[],int numtarefa,char atividade[][ativsize]){
	char desc[Maxtaf][tafdesc];
	int id[Maxtaf];
	int i, j;
	for(i = 0; i < numtarefa; ++i){
		strcpy(desc[i], task[i].desc);
		id[i] = i;
	}
	aquicksort(desc, id , 0, numtarefa -1);
	for(j = 0; j < i; j ++){
		printf("%d %s ", id[j]+1, atividade[task[id[j]].ativ]);
		printf("#%d %s\n",task[id[j]].prev,task[id[j]].desc);
	}
}

/* This function receives a list of ids and prints the id , activity, previewed
 * duration and description of the tasks that are represented by those ids in
 * the order that they are
 * */

void idprint(int list[], int n,struct tarefa task[], int num,char ativ[][ativsize]){
	int i, id, at;
	for(i = 0; i < n; ++i){
		id = list[i];
		if(id <= num && id > 0){
			at = task[id-1].ativ;
			printf("%d %s #%d", id, ativ[at], task[id-1].prev);
			printf(" %s\n", task[id-1].desc);
		}
		else printf("%d: no such task\n", id);
	}
}

/* This function creates a new user and adds it to the user list (utilizador) or
 * prints an error if there are too many users, or if theres already an user
 * with the same name that the inputed one
 * */
int newuser(char utilizador[][usersize], int num, char user[]){
	int i, state = 0;
	if(num == Maxuser){
		printf("too many users\n");
		state = 1;
	}
	else{
		for(i = 0; i < num; ++i){
	        	if(strcmp(utilizador[i], user) == 0){
		        	printf("user already exists\n");
				state = 1;
				break;
	        	}
		}
	}
	if(state == 0){
		strcpy(utilizador[num], user);
		++ num;
	}
	return num;
}

/* This function creates a new activity or prints an error if theres too many
 * activities already, if the inputed description is invalid,
 * or if theres already an activity with that name
 * */
int newativ(char atividade[][ativsize], int num, char ativ[]){
       	int i, state = 0, len = strlen(ativ);
	if(num == Maxativ){
		printf("too many activities\n");
		state = 1;
	}
	if(state == 0){
		for(i = 0; i < len; i++){
			if('a' <= ativ[i] && ativ[i] <= 'z'){
				printf("invalid description\n");
				state = 1;
				break;
			}
		}
	}

	if(state == 0){
		for(i = 0; i < num; i++){
			if(strcmp(atividade[i],ativ) == 0){
				printf("duplicate activity\n");
				state = 1;
				break;
			}
		}
	}
	if(state== 0){
		strcpy(atividade[num], ativ);
		++num;
	}
	return num;
}

/* Lists all existing users by order of creation */
void listuser(char utilizador[][usersize], int num){
	int i;
	for(i = 0; i < num; ++i){
		printf("%s\n", utilizador[i]);
	}
}

/* Lists all existing activities by order of creation */
void listativ(char atividade[][ativsize], int num){
	int i;
	for(i = 0;i < num; ++i){
		printf("%s\n", atividade[i]);
	}
}


/* This function receives the inputed command and by using auxiliary
 * functions it creates/lists all activities/users deppending on the
 * command and on the rest of the input
 * */
int listoradd(char atividade[][ativsize],char utilizador[][usersize],char command,int num){
	char user[usersize], ativ[ativsize], c;
	if((c = getchar()) != '\n'){
		if(command == 'a'){
			fgets(ativ, ativsize, stdin);
			ativ[strlen(ativ) - 1] = '\0';
			num = newativ(atividade, num, ativ);
		}
		else{
			scanf("%s", user);
			num = newuser(utilizador, num, user);
		}
	}
	else{
		if(command == 'a') listativ(atividade, num);
		else listuser(utilizador, num);
	}
	return num;
}


/* Auxiliary function that moves a task from a activity to another */
void taskmove(struct tarefa task[], int id, int user, int ativ, int tempo){
	int gasto, slack;
	if(task[id].ativ == 0){
		task[id].start = tempo;
	}
	if(ativ == 2 && task[id].ativ != 2){
		gasto = tempo - task[id].start;
		slack = gasto - task[id].prev;
		printf("duration=%d slack=%d\n", gasto, slack);
	}
	task[id].ativ = ativ;
	task[id].user = user;
}

/* This function analyses if its possible to move a task to one activity
 * to another, calling the Auxiliary function (taskmove) if it is possible
 * or printing an error if it isnt(if there is no task with the inputed id,
 * or if the inputed activity/user does not exist
 * */
void movetask(struct tarefa task[],char user[][usersize],char ativ[][ativsize],int u,int a,int t, int taf)
{
	int id, state = 0, i, indu = -1, inda = -1;
	char utilizador[usersize],atividade[ativsize];
	scanf("%d %s ", &id, utilizador);
	fgets(atividade, ativsize, stdin);
	atividade[strlen(atividade) - 1] = '\0';
	if(id > taf){
		printf("no such task\n");
		state = 1;
	}
	if(strcmp(atividade,ativ[task[id-1].ativ])==0){
	        state = 1;
	}
	if(strcmp(atividade, "TO DO") == 0 && state == 0){
		printf("task already started\n");
		state = 1;
	}
	if(state == 0){
		for( i = 0; i < u; ++i){
			if(strcmp(utilizador, user[i]) == 0){
				indu = i;
				break;
			}
		}
		if(indu == -1){
			printf("no such user\n");
			state = 1;
		}
	}
	if(state == 0){
		for(i = 0;i <a; i++){
			if(strcmp(atividade,ativ[i]) == 0){
				inda = i;
				break;
			}
		}
		if(inda == -1){
			printf("no such activity\n");
			state = 1;
		}
	}
	if(state == 0){
		taskmove(task, id - 1, indu, inda,t);
	}
}

/* This function lists all tasks that are in the inputed activity
 * by numeric order of there start time and alphabetic if
 * its the same value, or prints an error if
 * the inputed activity doesn't exist*/
void listativtaf(char ativ[][ativsize],int a,struct tarefa task[],int taf){
	char atividade[ativsize], taskdesc[Maxtaf][tafdesc];
	int i,j = 0,k,f; 
	int taskstart[Maxtaf],id[Maxtaf], numatividade = -1;
	getchar();
	fgets(atividade, ativsize, stdin);
	atividade[strlen(atividade) - 1] = '\0';
	for(i = 0; i < a ; ++i){
		if(strcmp(atividade, ativ[i]) == 0){
			numatividade = i;
			break;
		}
	}
	if(numatividade == -1) printf("no such activity\n");

	else{
		/*fill the id/taskstart with the ids and start times of all
		 * tasks in the inputed activity
		 * */
		for(i = 0; i < taf; i++){
			if(numatividade == task[i].ativ){
				taskstart[j] = task[i].start;
				id[j] = i;
				j++;
			}
		}
		if( j> 0){
			nquicksort(taskstart, id, 0, j -1);
			/* after sorting, the description of the task with the
			 * smaller id its put in a new array, where f its the 
			 * indice of the array to put the description on the
			 * array and also the smaller index for the quicksort */
			f = 0;
			strcpy(taskdesc[f], task[id[f]].desc);
			for(k = 1; k < j; k++){
				if(taskstart[f] == taskstart[k]){
					/* if the task has the same start value is
					 * puted in the array with the other one*/
					strcpy(taskdesc[k], task[id[k]].desc);
				}
				else{
					/* if it hasn't the same value the array
					 * is sorted alphabetically, then the 
					 * values are printed and the minimum
					 * of the array is changed to the
					 * current value of k and then 
					 * the new description is puted in the
					 * array*/
					aquicksort(taskdesc, id, f, k - 1);
					for(i = f; i <k; i++){
						printf("%d %d", id[i]+1, task[id[i]].start);
						printf(" %s\n", task[id[i]].desc);
					}
					f = k;
					strcpy(taskdesc[f], task[id[f]].desc);
				}
			}
			aquicksort(taskdesc, id, f, k - 1);
			for(i = f; i < k; i++){
				printf("%d %d", id[i]+1, task[id[i]].start);
				printf(" %s\n", task[id[i]].desc);
			}
		}
	}
}

/* Main function that reads the command and calls the function that does
 * what the command needs, or ends the program if the command q is inputed*/
int main(){
	/* tarefa, util e ativ represent the number of tasks, 
	 * users and activities respectively*/
	int state =0, tempo =0, tarefa =0, util =0,ativ =3;
	/* array of structures that contains all created tasks*/
	struct tarefa task[Maxtaf];
	/* array that contains all users names*/
	char user[Maxuser][usersize];
	/* array that contains all activities names*/
	char atividade[Maxativ][ativsize] = {"TO DO","IN PROGRESS","DONE"};

	while(state == 0){
		char command = getchar();
		
		if(command == 'q') state = 1;

		else if(command == 't'){
			tarefa = novatarefa(task, tarefa);
		}
		else if(command == 'l'){
			listtarefa(task, tarefa, atividade);
		}
		else if(command == 'n'){
			tempo = passartempo(tempo);
		}
		else if(command == 'u'){
			util = listoradd(atividade,user,command,util);
		}
		else if(command == 'a'){
			ativ = listoradd(atividade,user,command,ativ);
		}
		else if(command == 'm'){
			movetask(task, user, atividade, util, ativ, tempo, tarefa);
		}
		else if(command == 'd'){
			listativtaf(atividade, ativ, task, tarefa);
		}
	}
	return 0;
}
