# This makefile is defined to give you the following targets:
#
#    default: The default target: Compiles $(PROG) and whatever it 
#	   depends on.
#    style: Run our style checker on the project source files.  Requires that
#           the source files compile.
#    check: Compile $(PROG), if needed, and then for each file, F.in, in
#	   directory testing, use F.in as input to "java $(MAIN_CLASS)" and
#          compare the output to the contents of the file names F.out.
#          Report discrepencies.
#    clean: Remove all the .class files produced by java compilation, 
#          all Emacs backup files, and testing output files.
#
# In other words, type 'gmake' to compile everything; 'gmake check' to 
# compile and test everything, and 'gmake clean' to clean things up.
# 
# You can use this file without understanding most of it, of course, but
# I strongly recommend that you try to figure it out, and where you cannot,
# that you ask questions.  The Lab Reader contains documentation.

.PHONY: default check clean style unit acceptance

PACKAGE = jump61

STYLEPROG = style61b

JFLAGS = -g -Xlint:unchecked -Xlint:deprecation

CLASSDEST = ..

# All .java files in this directory.
SRCS := $(wildcard *.java)

# As a convenience, you can compile a single Java file X.java in this directory
# with 'make X.class'
%.class: %.java
	javac $(JFLAGS) -cp ..:$(CLASSPATH) $<

# First, and therefore default, target.
default: Main.class

style: default
	$(STYLEPROG) $(SRCS) 

check: unit acceptance

# Unit testing
unit: Main.class
	java -ea -cp ..:$(CLASSPATH) jump61.UnitTest

# Acceptance testing
acceptance:
	$(MAKE) -C .. check

# 'make clean' will clean up stuff you can reconstruct.
clean:
	$(RM) *~ *.class
	$(RM) -r classes

Main.class: $(SRCS)
	javac $(JFLAGS) -d $(CLASSDEST) $(SRCS)
