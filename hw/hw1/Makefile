
.PHONY: default check compile clean style

default: compile

compile:
	$(MAKE) -C Arithmetic
	$(MAKE) -C CompoundInterest
	$(MAKE) -C MultiArr
	$(MAKE) -C Flood

check:
	$(MAKE) -C Arithmetic check
	$(MAKE) -C CompoundInterest check
	$(MAKE) -C MultiArr check
	$(MAKE) -C Flood check

style:
	$(MAKE) -C Arithmetic style
	$(MAKE) -C CompoundInterest style
	$(MAKE) -C MultiArr style
	$(MAKE) -C Flood style

clean:
	$(MAKE) -C Arithmetic clean
	$(MAKE) -C CompoundInterest clean
	$(MAKE) -C MultiArr clean
	$(MAKE) -C Flood clean
