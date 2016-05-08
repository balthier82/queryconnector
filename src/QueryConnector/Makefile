###################################################################################
# QueryConnector - Attach a query to a Calc document							  #
# Copyright (C) 2013 Enrico Giuseppe Messina									  #
#																				  #
# This library is free software; you can redistribute it and/or					  #
# modify it under the terms of the GNU Lesser General Public					  #
# License as published by the Free Software Foundation; either					  #
# version 2.1 of the License, or (at your option) any later version.			  #
#																				  #
# This library is distributed in the hope that it will be useful,				  #
# but WITHOUT ANY WARRANTY; without even the implied warranty of				  #
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU				  #
# Lesser General Public License for more details.								  #
#																				  #
# You should have received a copy of the GNU Lesser General Public				  #
# License along with this library; if not, write to the Free Software			  #
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA  #
###################################################################################

PRJ=./
SETTINGS=makefiles

include $(SETTINGS)/settings.mk
include $(SETTINGS)/std.mk

#utility variables
EMPTY=
SPACE=$(EMPTY) $(EMPTY)

#commands
JAVAC=$(OO_SDK_JAVA_HOME)/bin/javac

#Directories
OUT_DIR=out
OUT_CLASS=$(OUT_DIR)/class
OUT_BIN=$(OUT_DIR)/bin
OUT_GEN=$(OUT_DIR)/misc
OUT_JOIN=$(OUT_DIR)/join

#sdk dir and files
SDK_IDL_DIR=$(OO_SDK_HOME)$(PS)idl

#project variables
PRJ_NAME=queryconnector

#java package
PACKAGE = com/meserico/queryconnector

#component variables
CMP_SERVICE_NAME=QueryConnectorService
CMP_NAME=$(PRJ_NAME)
CMP_RDB_NAME=$(CMP_NAME).uno.rdb
CMP_COMPONENTS=$(CMP_NAME).components

#output variables
CMP_BIN_OUT=$(OUT_BIN)
CMP_GEN_OUT=$(OUT_GEN)/$(CMP_NAME)
CMP_CLASS_OUT=$(OUT_CLASS)/$(CMP_NAME)
CMP_JOIN_OUT=$(OUT_JOIN)/$(CMP_NAME)

#derived output variables
CMP_UNOPKG_MANIFEST = $(CMP_GEN_OUT)/META-INF/manifest.xml
CMP_RDB=$(CMP_GEN_OUT)/$(CMP_RDB_NAME)
CMP_JAR_MANIFEST=$(CMP_GEN_OUT)/$(CMP_SERVICE_NAME).jar.manifest
CMP_LIB_PATH=$(CMP_JOIN_OUT)/$(CMP_NAME)
CMP_JAR=$(CMP_LIB_PATH)/$(CMP_NAME).jar

#extension variables
EXT_PKG = $(shell echo %cd%)/$(OUT_BIN)/$(PRJ_NAME).$(UNOOXT_EXT)

#files to include
IDLFILES = XQueryConnectorService.idl \
	QueryConnectorService.idl
	
CMP_JAVAFILES = \
	src/QueryConnectorService.java \
	src/QueryConnector.java

#java jars and classpath
LIBS_JARS = $(wildcard libs/*.jar) 
CLASSPATH_JARS = $(subst $(SPACE),$(PATH_SEPARATOR),$(LIBS_JARS))
TOTAL_CLASSPATH = $(CLASSPATH)$(PATH_SEPARATOR)$(CLASSPATH_JARS)

#class files
CMP_CLASSFILES = $(patsubst %.java,%.class,$(CMP_JAVAFILES))

#component files
GMP_GEN_TYPELIST = $(subst /,.,$(patsubst %.idl,-T$(PACKAGE)/% ,$(IDLFILES)))
CMP_GEN_URDFILES = $(patsubst %.idl,$(CMP_GEN_OUT)/%.urd,$(IDLFILES))

#-----------------------------------------------------------
# Targets
.PHONY: ALL
ALL : 
	@echo Creating output directories...
	@if not exist $(subst /,$(PS),$(CMP_BIN_OUT)) mkdir $(subst /,$(PS),$(CMP_BIN_OUT))
	@if not exist $(subst /,$(PS),$(CMP_CLASS_OUT)) mkdir $(subst /,$(PS),$(CMP_CLASS_OUT))
	@if not exist $(subst /,$(PS),$(CMP_GEN_OUT)) mkdir $(subst /,$(PS),$(CMP_GEN_OUT))
	@if not exist $(subst /,$(PS),$(CMP_JOIN_OUT)) mkdir $(subst /,$(PS),$(CMP_JOIN_OUT))
	@if not exist $(subst /,$(PS),$(CMP_LIB_PATH)) mkdir $(subst /,$(PS),$(CMP_LIB_PATH))
	
	@echo Generate class file from idl...
	@$(IDLC) -C -I. -I$(SDK_IDL_DIR) -O$(CMP_GEN_OUT) $(IDLFILES)
	@$(REGMERGE) $(CMP_RDB) /UCR $(CMP_GEN_URDFILES)
	@$(JAVAMAKER) -nD -O.$(PS)$(CMP_CLASS_OUT) $(CMP_RDB) -X$(URE_TYPES) -X$(OFFICE_TYPES)
	
	@echo Compile java classes...
	@"$(JAVAC)" -source 6 -target 6 -cp "$(TOTAL_CLASSPATH)$(PATH_SEPARATOR)$(CMP_CLASS_OUT)" -d $(CMP_CLASS_OUT) $(CMP_JAVAFILES)
	
	@echo Generate java manifest for component...
	@echo UNO-Type-Path: $(CMP_NAME).jar> $(CMP_JAR_MANIFEST)
	@echo RegistrationClassName: $(CMP_SERVICE_NAME)>> $(CMP_JAR_MANIFEST)
	@echo Class-Path: JSONLibrary.jar LODBConnector.jar>> $(CMP_JAR_MANIFEST)
	
	@echo Copy license...
	@if not exist $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)license) mkdir $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)license)
	@copy license$(PS)lgpl.txt $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)license)
	
	@echo Copy descriptions...
	@if not exist $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)description) mkdir $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)description)
	@copy description$(PS)desc_en.txt $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)description)
	@copy description$(PS)desc_it.txt $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)description)
	
	@echo Creating jar in $(CMP_JOIN_OUT) directory...
	@$(SDK_JAR) cfm $(CMP_JAR) $(CMP_JAR_MANIFEST) -C $(CMP_CLASS_OUT) .
	
	@echo Copying icons and images...
	@if not exist $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)images) @mkdir $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)images)
	@if not exist $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)icons) @mkdir $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)icons)
	@xcopy /E /H /Y icons $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)icons)
	@xcopy /E /H /Y images $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)images)
	
	@echo Copy libs...
	@xcopy /E /H /Y libs $(subst /,$(PS),$(CMP_LIB_PATH))
	
	@echo Copyng other files...
	@copy Addons.xcu $(subst /,$(PS),$(CMP_JOIN_OUT))
	@copy description.xml $(subst /,$(PS),$(CMP_JOIN_OUT))
	@if not exist $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)META-INF) mkdir $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)META-INF)
	@copy manifest.xml $(subst /,$(PS),$(CMP_JOIN_OUT)$(PS)META-INF)
	@copy $(subst /,$(PS),$(CMP_RDB)) $(subst /,$(PS),$(CMP_JOIN_OUT))
	@copy queryconnector.components $(subst /,$(PS),$(CMP_JOIN_OUT))
	@copy parcel-descriptor.xml $(subst /,$(PS),$(CMP_LIB_PATH))
	
	@echo Creating $(UNOOXT_EXT) package...
	@if exist $(subst /,$(PS),$(EXT_PKG)) del /F $(subst /,$(PS),$(EXT_PKG))
	@cd $(CMP_JOIN_OUT) && $(SDK_ZIP) -9 -r -o -q $(EXT_PKG) .
	
	@echo complete.
