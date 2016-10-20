#!bin/bash
JARFILE="TODOOOOOOOO"
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

# java -jar ./$JARFILE $OUT $SEED $NPAQ $PROP $FEL $OPE $GEN $ALG $ITE $STE $K $LAM >> $DIRECTORIES[1]/$EXPNAME
# ./$RANDFILE $RANDSEED $NUMEXPERIMENTOS >> $DIRECTORIES[0]/$EXPNAME

OUT="" # 0: coste 1: tiempo
SEED=""
NPAQS=""
PROP=""
FEL=""
OPE="" 
GEN=""
ALG="" # 0: hill climbing 1: simmulated annealing
ITE=""
STE=""
K=""
LAM=""

SEEDFILE="GENERAL"
NUMEXPERIMENTOS="1000"
./$RANDFILE $NUMEXPERIMENTOS >> ${DIRECTORIES[0]}/$SEEDFILE

# EXPERIMENTO 1: Determinar mejor conjunto de operadores ################################################
EXPNAME="OPERADORES"
OUT="0"
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
    OPE=$operador
    while read s; do
        SEED=$s
        java -jar ./$JARFILE $OUT $SEED $NPAQ $PROP $FEL $OPE $GEN $ALG >> ${DIRECTORIES[1]}/$EXPNAME$OPE
    done <${DIRECTORIES[0]}/$SEEDFILE
done

if [ -f ${DIRECTORIES[2]}/$EXPNAME ]; then
    rm ${DIRECTORIES[2]}/$EXPNAME
fi
echo "#!/usr/bin/env Rscript" >> ${DIRECTORIES[2]}/$EXPNAME
echo "a = scan(\"../${DIRECTORIES[1]}/${EXPNAME}_0\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "b = scan(\"../${DIRECTORIES[1]}/${EXPNAME}_1\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "if (mean(a) < mean(b)) {" >> ${DIRECTORIES[2]}/$EXPNAME
echo "cat(\"1\", \"\n\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "} else {" >> ${DIRECTORIES[2]}/$EXPNAME
echo "cat(\"0\", \"\n\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "}" >> ${DIRECTORIES[2]}/$EXPNAME
chmod +x ${DIRECTORIES[2]}/$EXPNAME
OP=$(./${DIRECTORIES[2]}/$EXPNAME)

# EXPERIMENTO 2: Determinar mejor estrategia de generación de solución inicial ##########################
EXPNAME="GENERADOR"
NPAQS="100"
PROP="1.2"
FEL="0"
ALG="0"
# ---------------------------------------
GENERADORES=(0 1);
# ---------------------------------------
for generador in "${GENERADORES[@]}" 
do
    GEN=$generador
    while read s; do
        SEED=$s
        java -jar ./$JARFILE $OUT $SEED $NPAQ $PROP $FEL $OPE $GEN $ALG >> ${DIRECTORIES[1]}/$EXPNAME$GEN
    done <${DIRECTORIES[0]}/$SEEDFILE
done

if [ -f ${DIRECTORIES[2]}/$EXPNAME ]; then
    rm ${DIRECTORIES[2]}/$EXPNAME
fi
echo "#!/usr/bin/env Rscript" >> ${DIRECTORIES[2]}/$EXPNAME
echo "a = scan(\"../${DIRECTORIES[1]}/${EXPNAME}_0\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "b = scan(\"../${DIRECTORIES[1]}/${EXPNAME}_1\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "if (mean(a) < mean(b)) {" >> ${DIRECTORIES[2]}/$EXPNAME
echo "cat(\"1\", \"\n\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "} else {" >> ${DIRECTORIES[2]}/$EXPNAME
echo "cat(\"0\", \"\n\")" >> ${DIRECTORIES[2]}/$EXPNAME
echo "}" >> ${DIRECTORIES[2]}/$EXPNAME
chmod +x ${DIRECTORIES[2]}/$EXPNAME
GEN=$(./${DIRECTORIES[2]}/$EXPNAME)

# EXPERIMENTO 3: Determinar mejores parametros para simulated annealing ##################################
EXPNAME="SIMULATEDKLAMB"
OUT="0"
NPAQS="100"
PROP="1.2"
FEL="0"
ALG="1"
ITE="300000"
STE="3"

k_ini=""
k_iteraciones="100"
k_incremento="0.2"
lambda_ini=""
lambda_iteraciones="100"
lambda_incremento="50"

K=$k_ini
i="1"
while [ "$i" -le "$k_iteraciones" ]; do
    j="1"
    LAMDA=$k_ini
    while [ "$j" -le "$lambda_iteraciones" ]; do
        SEED=$(./$RANDFILE 1)
        cost=$(java -jar ./$JARFILE $OUT $SEED $NPAQ $PROP $FEL $OPE $GEN $ALG $ITE $STE $K $LAM)
        echo "$SEED" >> ${DIRECTORIES[0]}/$EXPNAME
        echo "$K" >> "${DIRECTORIES[3]}/${EXPNAME}_k"
        echo "$LAMBDA" >> "${DIRECTORIES[3]}/${EXPNAME}_lambda"
        echo "$cost" >> "${DIRECTORIES[1]}/${EXPNAME}"
        j=$(($j + 1))
        LAMBDA=$(echo "$LAMBDA + $lambda_incremento" | bc)
    done
    i=$(($i + 1))
    K=$(echo "$K + $k_incremento" | bc)
done

# EXPERIMENTO 4: Estudiar como evoluciona el tiempo de ejecución según el número de paquetes #############
# y la proporción de peso transportable
EXPNAME="PAQSPROP"
OUT="1"
FEL="0"
ALG="0"
prop="1.2"
prop_iteraciones="100"
prop_incremento="0.2"
paqs="100"
paqs_iteraciones="100"
paqs_incremento="50"
i="1"
while [ "$i" -le "$prop_iteraciones" ]; do
    PROP=$prop
    j="1"
    paqs="100"
    while [ "$j" -le "$paqs_iteraciones" ]; do
        SEED=$(./$RANDFILE 1)
        NPAQS=$paqs
        time=$(java -jar ./$JARFILE $OUT $SEED $NPAQ $PROP $FEL $OPE $GEN $ALG)
        echo "$SEED" >> ${DIRECTORIES[0]}/$EXPNAME
        echo "$prop" >> "${DIRECTORIES[3]}/${EXPNAME}_prop"
        echo "$paqs" >> "${DIRECTORIES[3]}/${EXPNAME}_paqs"
        echo "$time" >> "${DIRECTORIES[1]}/${EXPNAME}"
        j=$(($j + 1))
        paqs=$(echo "$paqs + $paqs_incremento" | bc)
    done
    i=$(($i + 1))
    prop=$(echo "$prop + $prop_incremento" | bc)
done


# EXPERIMENTO 6: Como afecta la felicidad de los usuarios al coste de transporte y almacenamiento
EXPNAME="HAPPINESS"
OUT="0"
FEL="0"
ALG="0"
fel_incremento="0.2"
NPAQS="100"
PROP="1.2"
fel_iteraciones="5000"
i="1"
echo "FEL\tCOST" >> ${DIRECTORIES[1]}/$EXPNAME
while [ "$i" -le "$paqs_iteraciones" ]; do
    SEED=$(./$RANDFILE 1)
    cost=$(java -jar ./$JARFILE $OUT $SEED $NPAQ $PROP $FEL $OPE $GEN $ALG)
    echo "$SEED" >> ${DIRECTORIES[0]}/$EXPNAME
    echo "$FEL" >> "${DIRECTORIES[1]}/${EXPNAME}"
    echo "$cost" >> "${DIRECTORIES[1]}/${EXPNAME}"
    i=$(($i + 1))
    FEL=$(echo "$FEL + $fel_incremento" | bc)
done
# Repetir experimentos con simulated annealing
EXPNAME="HAPPINESS_annealing"
OUT="0"
FEL="0"
ALG="1"
fel_incremento="0.2"
NPAQS="100"
PROP="1.2"
fel_iteraciones="5000"
i="1"
echo "FEL\tCOST" >> ${DIRECTORIES[1]}/$EXPNAME
while [ "$i" -le "$paqs_iteraciones" ]; do
    SEED=$(./$RANDFILE 1)
    cost=$(java -jar ./$JARFILE $OUT $SEED $NPAQ $PROP $FEL $OPE $GEN $ALG)
    echo "$SEED" >> ${DIRECTORIES[0]}/$EXPNAME
    echo "$FEL" >> "${DIRECTORIES[1]}/${EXPNAME}"
    echo "$cost" >> "${DIRECTORIES[1]}/${EXPNAME}"
    i=$(($i + 1))
    FEL=$(echo "$FEL + $fel_incremento" | bc)
done
