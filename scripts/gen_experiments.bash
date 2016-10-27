#!bin/bash
JARFILE="AI_LocalSearch.jar"
RANDFILE="seed_generator"
DIRECTORIES=(SEEDS RAWRESULTS RSCRIPTS PARAMS)
for dir in "${DIRECTORIES[@]}"
do
    if [ -d $dir ]; then
        mv --backup=t "$dir" ".$dir.bkp"
    fi
    mkdir "$dir"
done

g++ -o ${RANDFILE} ${RANDFILE}.cpp

# java -jar ./$JARFILE $OUT $SEED $NPAQS $PROP $FEL $OPE $GEN $ALG $ITE $STE $K $LAM >> $DIRECTORIES[1]/$EXPNAME
# ./$RANDFILE $RANDSEED $NUMEXPERIMENTOS >> $DIRECTORIES[0]/$EXPNAME
OUT="1" # 0: coste 1: tiempo
SEED="1"
NPAQS="1"
PROP="1"
FEL="1"
OPE="1" 
GEN="1"
ALG="1" # 0: hill climbing 1: simmulated annealing
ITE="1"
STE="1"
K="1"
LAM="1"

SEEDFILE="GENERAL"
NUMEXPERIMENTOS="1000"
./$RANDFILE $NUMEXPERIMENTOS >> ${DIRECTORIES[0]}/$SEEDFILE

# EXPERIMENTO 1: Determinar mejor conjunto de operadores ################################################
EXPNAME="OPERADORES"
echo "EXP: $EXPNAME ######################################################"
OUT="1"
NPAQS="100"
PROP="1.2"
FEL="0"
GEN="0"
ALG="0"
# ---------------------------------------
OPERADORES=(0 1);
# ---------------------------------------

for operador in "${OPERADORES[@]}" 
do
    i="1"
    OPE=$operador
    while read s; do
        SEED=$s
        echo "$EXPNAME$operador out=$OUT seed=$SEED paqs=$NPAQS prop=$PROP fel=$FEL op=$OPE gen=$GEN alg=$ALG ite=$ITE st=$STE k=$K lam=$LAM"
        cost=$(java -jar $JARFILE $OUT $SEED $NPAQS $PROP $FEL $OPE $GEN $ALG $ITE $STE $K $LAM)
        echo $cost >> ${DIRECTORIES[1]}/$EXPNAME$OPE
        echo "$i: $cost"
        i=$(($i + 1))
    done <${DIRECTORIES[0]}/$SEEDFILE
done

if [ -f ${DIRECTORIES[2]}/$EXPNAME ]; then
    rm ${DIRECTORIES[2]}/$EXPNAME
fi
echo "#!/usr/bin/env Rscript" >> ${DIRECTORIES[2]}/$EXPNAME
echo "a = scan(\"./${DIRECTORIES[1]}/${EXPNAME}0\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "b = scan(\"./${DIRECTORIES[1]}/${EXPNAME}1\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "if (mean(a) < mean(b)) {" >> ${DIRECTORIES[2]}/$EXPNAME
echo "cat(\"0\", \"\n\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "} else {" >> ${DIRECTORIES[2]}/$EXPNAME
echo "cat(\"1\", \"\n\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "}" >> ${DIRECTORIES[2]}/$EXPNAME
chmod +x ${DIRECTORIES[2]}/$EXPNAME
OP=$(./${DIRECTORIES[2]}/$EXPNAME)

# EXPERIMENTO 2: Determinar mejor estrategia de generación de solución inicial ##########################
EXPNAME="GENERADOR"
echo "EXP: $EXPNAME ######################################################"
OUT="0"
NPAQS="100"
PROP="1.2"
FEL="0"
ALG="0"
# ---------------------------------------
GENERADORES=(0 1);
# ---------------------------------------

for generador in "${GENERADORES[@]}" 
do
    i="1"
    GEN=$generador
    while read s; do
        SEED=$s
        echo "$EXPNAME$generador out=$OUT seed=$SEED paqs=$NPAQS prop=$PROP fel=$FEL op=$OPE gen=$GEN alg=$ALG ite=$ITE st=$STE k=$K lam=$LAM"
        cost=$(java -jar $JARFILE $OUT $SEED $NPAQS $PROP $FEL $OPE $GEN $ALG $ITE $STE $K $LAM)
        echo $cost >> ${DIRECTORIES[1]}/$EXPNAME$GEN
        echo "$i: $cost"
        i=$(($i + 1))
    done <${DIRECTORIES[0]}/$SEEDFILE
done

if [ -f ${DIRECTORIES[2]}/$EXPNAME ]; then
    rm ${DIRECTORIES[2]}/$EXPNAME
fi
echo "#!/usr/bin/env Rscript" >> ${DIRECTORIES[2]}/$EXPNAME
echo "a = scan(\"./${DIRECTORIES[1]}/${EXPNAME}0\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "b = scan(\"./${DIRECTORIES[1]}/${EXPNAME}1\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "if (mean(a) < mean(b)) {" >> ${DIRECTORIES[2]}/$EXPNAME
echo "cat(\"0\", \"\n\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "} else {" >> ${DIRECTORIES[2]}/$EXPNAME
echo "cat(\"1\", \"\n\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "}" >> ${DIRECTORIES[2]}/$EXPNAME
chmod +x ${DIRECTORIES[2]}/$EXPNAME
GEN=$(./${DIRECTORIES[2]}/$EXPNAME)

# EXPERIMENTO 3: Determinar mejores parametros para simulated annealing ##################################
EXPNAME="SIMULATEDKLAMB"
echo "EXP: $EXPNAME ######################################################"
OUT="0"
NPAQS="100"
PROP="1.2"
FEL="0"
ALG="1"
ITE="300000"
STE="3"

k_ini="500"
k_iteraciones="100"
k_incremento="500"
lambda_ini="0.001"
lambda_iteraciones="100"
lambda_incremento="0.001"

K="$k_ini"
i="1"
while [ "$i" -le "$k_iteraciones" ]; do
    j="1"
    LAM="$lambda_ini"
    while [ "$j" -le "$lambda_iteraciones" ]; do
        SEED=$(./$RANDFILE 1)
        echo "$EXPNAME $i $j out=$OUT seed=$SEED paqs=$NPAQS prop=$PROP fel=$FEL op=$OPE gen=$GEN alg=$ALG ite=$ITE st=$STE k=$K lam=$LAM"
        cost=$(java -jar $JARFILE $OUT $SEED $NPAQS $PROP $FEL $OPE $GEN $ALG $ITE $STE $K $LAM)
        echo "$SEED" >> ${DIRECTORIES[0]}/$EXPNAME
        echo "$K" >> "${DIRECTORIES[3]}/${EXPNAME}_k"
        echo "$LAM" >> "${DIRECTORIES[3]}/${EXPNAME}_lambda"
        echo "$cost" >> "${DIRECTORIES[1]}/${EXPNAME}"
        echo "k=$K lam=$LAM cost=$cost"
        j=$(($j + 1))
        LAM=$(echo "$LAM + $lambda_incremento" | bc)
    done
    i=$(($i + 1))
    K=$(echo "$K + $k_incremento" | bc)
done

# EXPERIMENTO 4: Estudiar como evoluciona el tiempo de ejecución según el número de paquetes #############
# y la proporción de peso transportable
EXPNAME="PAQSPROP"
echo "EXP: $EXPNAME ######################################################"
OUT="1"
FEL="0"
ALG="0"
prop="1.2"
prop_iteraciones="100"
prop_incremento="0.05"
paqs="100"
paqs_iteraciones="100"
paqs_incremento="5"
i="1"
while [ "$i" -le "$prop_iteraciones" ]; do
    PROP=$prop
    j="1"
    paqs="100"
    while [ "$j" -le "$paqs_iteraciones" ]; do
        SEED=$(./$RANDFILE 1)
        NPAQS=$paqs
        echo "$EXPNAME $i $j out=$OUT seed=$SEED paqs=$NPAQS prop=$PROP fel=$FEL op=$OPE gen=$GEN alg=$ALG ite=$ITE st=$STE k=$K lam=$LAM"
        time=$(java -jar $JARFILE $OUT $SEED $NPAQS $PROP $FEL $OPE $GEN $ALG $ITE $STE $K $LAM)
        echo "$SEED" >> ${DIRECTORIES[0]}/$EXPNAME
        echo "$prop" >> "${DIRECTORIES[3]}/${EXPNAME}_prop"
        echo "$paqs" >> "${DIRECTORIES[3]}/${EXPNAME}_paqs"
        echo "$time" >> "${DIRECTORIES[1]}/${EXPNAME}"
        echo "prop=$prop paqs=$paqs time=$time"
        j=$(($j + 1))
        paqs=$(echo "$paqs + $paqs_incremento" | bc)
    done
    i=$(($i + 1))
    prop=$(echo "$prop + $prop_incremento" | bc)
done


# EXPERIMENTO 6: Como afecta la felicidad de los usuarios al coste de transporte y almacenamiento
EXPNAME="HAPPINESS"
echo "EXP: $EXPNAME ######################################################"
OUT="0"
FEL="0"
ALG="0"
fel_incremento="0.2"
NPAQS="100"
PROP="1.2"
fel_iteraciones="1000"
i="1"
while [ "$i" -le "$paqs_iteraciones" ]; do
    SEED=$(./$RANDFILE 1)
    echo "$EXPNAME $i out=$OUT seed=$SEED paqs=$NPAQS prop=$PROP fel=$FEL op=$OPE gen=$GEN alg=$ALG ite=$ITE st=$STE k=$K lam=$LAM"
    cost=$(java -jar $JARFILE $OUT $SEED $NPAQS $PROP $FEL $OPE $GEN $ALG $ITE $STE $K $LAM)
    echo "$SEED" >> ${DIRECTORIES[0]}/$EXPNAME
    echo "$FEL" >> "${DIRECTORIES[3]}/${EXPNAME}_fel"
    echo "$cost" >> "${DIRECTORIES[1]}/${EXPNAME}"
    echo "fel=$FEL cost=$cost"
    i=$(($i + 1))
    FEL=$(echo "$FEL + $fel_incremento" | bc)
done
# Repetir experimentos con simulated annealing
EXPNAME="HAPPINESS_annealing"
echo "EXP: $EXPNAME ######################################################"
OUT="0"
FEL="0"
ALG="1"
fel_incremento="0.2"
NPAQS="100"
PROP="1.2"
fel_iteraciones="1000"
i="1"
while [ "$i" -le "$paqs_iteraciones" ]; do
    SEED=$(./$RANDFILE 1)
    echo "$EXPNAME $i out=$OUT seed=$SEED paqs=$NPAQS prop=$PROP fel=$FEL op=$OPE gen=$GEN alg=$ALG ite=$ITE st=$STE k=$K lam=$LAM"
    cost=$(java -jar $JARFILE $OUT $SEED $NPAQS $PROP $FEL $OPE $GEN $ALG $ITE $STE $K $LAM)
    echo "$SEED" >> ${DIRECTORIES[0]}/$EXPNAME
    echo "$FEL" >> "${DIRECTORIES[3]}/${EXPNAME}_fel"
    echo "$cost" >> "${DIRECTORIES[1]}/${EXPNAME}"
    echo "fel=$FEL cost=$cost
    i=$(($i + 1))
    FEL=$(echo "$FEL + $fel_incremento" | bc)
done
