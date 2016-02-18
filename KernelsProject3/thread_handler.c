#include <stdio.h>
#include <assert.h>

#include "alarm_handler.h"
#include "thread_handler.h"
#include "sys/alt_stdio.h"
#include <stdlib.h>

/* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */
/* The two macros are extremely useful by turnning on/off interrupts when atomicity is required */

#define QUEUE_SIZE 12
#define DISABLE_INTERRUPTS() {  \
    asm("wrctl status, zero");  \
}

#define ENABLE_INTERRUPTS() {   \
    asm("movi et, 1");          \
    asm("wrctl status, et");    \
}


tcb * readyQ[12] = {NULL};
int readyQIndex = 0;
int readyQdequeue = 0;



int getQSize(tcb ** QUEUE){
	int n = QUEUE_SIZE;
	int i;
	int count = 0;
	for (i=0; i<n; i++){
		tcb * check = QUEUE[i];
		if (check != NULL){
			count++;
		}
	}
	return count;
}

void enqueue(tcb ** QUEUE, tcb * TCB, int * indexVar){
	//Find first empty spot, throw it in the queue
	int n = QUEUE_SIZE;
	while (1){
		if (QUEUE[*indexVar] == NULL){
			QUEUE[*indexVar] = TCB;
			return;
		} else {
			*indexVar = (*indexVar + 1) % n;
		}
	}
}

tcb * dequeue(tcb ** QUEUE, int * dequeueIndex){
	if (QUEUE[*dequeueIndex] != NULL){
		tcb * returnee = QUEUE[*dequeueIndex];
		QUEUE[*dequeueIndex] = NULL;
		*dequeueIndex = (*dequeueIndex + 1) % QUEUE_SIZE;
		return returnee;
	} else {
		alt_printf("dequeue index is screwed up!!\n");
		return NULL;
	}
}

/* the current running thread */
static tcb *current_running_thread      = NULL;

/* pointing to the stack/context of main() */
static unsigned int *main_stack_pointer = NULL;

tcb *mythread_create(unsigned int tid, unsigned int stack_size, void (*mythread)(unsigned int tid))
{
    unsigned int *tmp_ptr;
    
    /* allocate a tcb for a thread */
    tcb *thread_pointer;
    
    thread_pointer                      = (tcb *)malloc(sizeof(tcb));
    if (thread_pointer == NULL)
    {
        printf("Unable to allocate space!\n");
        exit(1);
    }
    
    /* initialize the thread's tcb */
    thread_pointer->tid                 = tid;
    thread_pointer->stack               = (unsigned int *)malloc(sizeof(unsigned int) * stack_size);
    if (thread_pointer->stack == NULL)
    {
        printf("Unable to allocate space!\n");
        exit(1);
    }
    thread_pointer->stack_size          = stack_size;
    thread_pointer->stack_pointer       = (unsigned int *)(thread_pointer->stack + stack_size - 19);
    thread_pointer->state               = NEW;
    
    /* initialize the thread's stack */
    tmp_ptr                             = thread_pointer->stack_pointer;
    tmp_ptr[18]                         = (unsigned int)mythread;                               // ea
    tmp_ptr[17]                         = 1;                                                    // estatus
    tmp_ptr[5]                          = tid;                                                  // r4
    tmp_ptr[0]                          = (unsigned int)mythread_cleanup;                       // ra
    tmp_ptr[-1]                         = (unsigned int)(thread_pointer->stack + stack_size);   // fp
           
    return thread_pointer;
}

/* NEW ----> READY */
void mythread_start(tcb *thread_pointer)
{
    // assert(thread_pointer && thread_pointer->state == NEW);
    thread_pointer->state = READY;
}

/* READY --push into--> readyQ */
void mythread_join(tcb *thread_pointer)
{
    // assert(thread_pointer && thread_pointer->state == READY);
	thread_pointer->state=READY;
    enqueue(readyQ, thread_pointer, &readyQIndex);
}

/* RUNNING ----> BLOCKED */
void mythread_block(tcb *thread_pointer)
{
    // assert(thread_pointer && thread_pointer->state == RUNNING);
    thread_pointer->state = BLOCKED;
}

/* RUNNING ----> TERMINATED */
void mythread_terminate(tcb *thread_pointer)
{
    // assert(thread_pointer && thread_pointer->state == RUNNING);
    thread_pointer->state = TERMINATED;
}

void *mythread_schedule(void *context)
{
    if (getQSize(readyQ) > 0)
    {
        if (current_running_thread != NULL)
        {
            // assert(current_running_thread->state == RUNNING);
            // assert(main_stack_pointer != NULL);
            current_running_thread->state = READY;
            current_running_thread->stack_pointer = (unsigned int *)context;
            enqueue(readyQ, current_running_thread, &readyQIndex);
        }
        else if (main_stack_pointer == NULL)
        {
            main_stack_pointer = (unsigned int *)context;
        }
        
        current_running_thread = dequeue(readyQ, &readyQdequeue);
        // assert(current_running_thread->state == READY);
        current_running_thread->state = RUNNING;
        
        context = (void *)(current_running_thread->stack_pointer);
    }
    else if (current_running_thread==NULL && main_stack_pointer!=NULL)
    {        
        context = (void *)main_stack_pointer;
    }

    return context;
}

//For now use this to check if readyQ is empty
unsigned int mythread_isQempty()
{
    return (getQSize(readyQ) == 0) && (current_running_thread == NULL);
}

void mythread_cleanup()
{
    DISABLE_INTERRUPTS();
    mythread_terminate(current_running_thread);
    free(current_running_thread->stack);
    free(current_running_thread);
    current_running_thread = NULL;
    ENABLE_INTERRUPTS();
    while(1);
}
