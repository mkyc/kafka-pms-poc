#!/bin/bash

SIZE=10
SECONDS=0
COUNTER=0

echo "start $(date +%H%M%S)"

for i in `seq 1 ${SIZE}`;
do 
	PROJECT=$(curl -sI -X POST http://localhost:8080/projects/ | tr -d '\r' | grep Location | awk -F ": " '{print $2}')
	((COUNTER++))
	sleep .1
	DP=$(date +%H%M%S)
	echo "$DP project $PROJECT"
	for j in `seq 1 ${SIZE}`;
	do
		RENAME_COMMAND="curl -d \"name=test$j\" -X PATCH $PROJECT"
		eval ${RENAME_COMMAND}
		((COUNTER++))
	done
	for k in `seq 1 ${SIZE}`;
    do
        ADD_TASK_COMMAND="curl -sI -X POST $PROJECT/tasks?name=task | tr -d '\r' | grep Location | awk -F \": \" '{print \$2}'"
        TASK=$(eval ${ADD_TASK_COMMAND})
        ((COUNTER++))
        DT=$(date +%H%M%S)
        echo "$DT task $TASK"
        for l in `seq 1 ${SIZE}`;
        do
            RENAME_TASK_COMMAND="curl -X PUT $TASK?name=task$l"
            eval ${RENAME_TASK_COMMAND}
            ((COUNTER++))
        done
    done
done

echo "finish $(date +%H%M%S)"
echo "$COUNTER operations took $SECONDS seconds"
