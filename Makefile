JFLAGS = -g
JC = javac
JVM= java

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = atoms/Atom.java \
		atoms/AtomType.java \
		reduction/Graph.java \
		reduction/GraphOperator.java \
		reduction/MainClass.java
		
MAIN = reduction/MainClass

default: classes

build: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)
	
clean:
	find . -name "*.class" -type f -delete