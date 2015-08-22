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

# Define non-platform/compiler specific settings
SAMPLE_NAME=queryconnector
SAMPLE_CLASS_OUT = $(OUT_CLASS)/$(SAMPLE_NAME)
SAMPLE_GEN_OUT = $(OUT_MISC)

SCRIPT_NAME=queryconnector
SCRIPT_CLASS_OUT=$(SAMPLE_CLASS_OUT)/$(SCRIPT_NAME)
SCRIPT_GEN_OUT=$(SAMPLE_GEN_OUT)/$(SCRIPT_NAME)
SCRIPT_JAR_NAME=$(SCRIPT_NAME).jar
SCRIPT_JAR=$(SAMPLE_CLASS_OUT)/$(SCRIPT_JAR_NAME)
SCRIPT_PACKAGE = $(OUT_BIN)/$(SCRIPT_NAME).$(UNOOXT_EXT)
SCRIPT_PACKAGE_URL = $(subst \\,\,"$(COMP_PACKAGE_DIR)$(PS)$(SCRIPT_NAME).$(UNOOXT_EXT)")
SCRIPT_REGISTERFLAG=$(SCRIPT_GEN_OUT)$(PS)$(SCRIPT_NAME)_register_scriptpkg.flag


# Define non-platform/compiler specific settings
COMP_NAME=QueryConnectorService
COMP_CLASS_OUT=$(OUT_CLASS)/$(COMP_NAME)
COMP_GEN_OUT=$(OUT_MISC)/$(COMP_NAME)
COMP_RDB_NAME=$(COMP_NAME).uno.rdb
COMP_RDB=$(COMP_GEN_OUT)/$(COMP_RDB_NAME)
COMP_PACKAGE=$(OUT_BIN)/$(COMP_NAME).$(UNOOXT_EXT)
COMP_PACKAGE_URL=$(subst \\,\,"$(COMP_PACKAGE_DIR)$(PS)$(COMP_NAME).$(UNOOXT_EXT)")
COMP_JAR_NAME=$(COMP_NAME).uno.jar
COMP_JAR=$(COMP_CLASS_OUT)/$(COMP_JAR_NAME)
COMP_JAR_MANIFEST=$(COMP_CLASS_OUT)/$(COMP_NAME).uno.Manifest
COMP_UNOPKG_MANIFEST = $(COMP_GEN_OUT)/META-INF/manifest.xml
COMP_REGISTERFLAG=$(COMP_GEN_OUT)$(PS)java_$(COMP_NAME)_register_component.flag
COMP_COMPONENTS=$(COMP_NAME).components

IDLFILES = QueryConnectorService.idl \
	XQueryConnectorService.idl

JAVAFILES  = \
	queryconnector/QueryConnector.java
	
COMP_JAVAFILES = \
	queryconnector/QueryConnectorService.java \
	queryconnector/QueryConnector.java
	
PACKAGE = com/meserico

CLASSFILES = $(patsubst %.java,$(SCRIPT_CLASS_OUT)/%.class,$(subst $(SAMPLE_NAME)/,,$(JAVAFILES)))
CLASSFILES_FOR_COMP = $(patsubst %.java,../../../$(SCRIPT_CLASS_OUT)/%.class,$(subst $(SAMPLE_NAME)/,,$(JAVAFILES)))
COMP_CLASSFILES = $(patsubst %.java,$(COMP_CLASS_OUT)/%.class,$(COMP_JAVAFILES))

GEN_CLASSFILES = $(patsubst %.idl,$(COMP_CLASS_OUT)/$(PACKAGE)/%.class,$(IDLFILES))
GEN_TYPELIST = $(subst /,.,$(patsubst %.idl,-T$(PACKAGE)/% ,$(IDLFILES)))
GEN_URDFILES = $(patsubst %.idl,$(COMP_GEN_OUT)/%.urd,$(IDLFILES))

$(COMP_NAME)_CLASSFILES = $(COMP_NAME).class \
		$(COMP_NAME)$(QUOTE)$$$(COMP_NAME)Impl.class

$(COMP_NAME)_CLASSFILES += $(subst $(COMP_CLASS_OUT)/,,$(GEN_CLASSFILES))

SDK_CLASSPATH = $(subst $(EMPTYSTRING) $(PATH_SEPARATOR),$(PATH_SEPARATOR),$(CLASSPATH)\
		$(PATH_SEPARATOR)$(SAMPLE_CLASS_OUT)$(PATH_SEPARATOR)$(COMP_CLASS_OUT))

# Targets
.PHONY: ALL
ALL : QueryConnectorMake 

include $(SETTINGS)/stdtarget.mk

#component

$(COMP_CLASS_OUT)/%.Manifest :
	-$(MKDIR) $(subst /,$(PS),$(@D))
	@echo UNO-Type-Path: $(basename $*).uno.jar> $@
	@echo RegistrationClassName: $(basename $(basename $(@F)))>> $@
	@echo Class-Path: queryconnector/JSONLibrary.jar queryconnector/LODBConnector.jar>> $@
	
$(COMP_GEN_OUT)/%.urd : %.idl
	-$(MKDIR) $(subst /,$(PS),$(@D))
	$(IDLC) -C -I. -I$(IDL_DIR) -O$(COMP_GEN_OUT) $<
	
$(COMP_GEN_OUT)/%.rdb : $(GEN_URDFILES)
	-$(DEL) $(subst \\,\,$(subst /,$(PS),$@))
	-$(MKDIR) $(subst /,$(PS),$(@D))
	$(REGMERGE) $@ /UCR $(GEN_URDFILES)

$(GEN_CLASSFILES) : $(COMP_RDB)
	-$(MKDIR) $(subst /,$(PS),$(@D))
	$(JAVAMAKER) -nD $(GEN_TYPELIST) -O$(COMP_CLASS_OUT) $(COMP_RDB) -X$(URE_TYPES) -X$(OFFICE_TYPES)

$(COMP_CLASSFILES) : $(COMP_JAVAFILES) $(GEN_CLASSFILES)
	-$(MKDIR) $(subst /,$(PS),$(@D))
	$(SDK_JAVAC) $(JAVAC_FLAGS) -classpath "$(SDK_CLASSPATH);QueryConnector\LODBConnector.jar;QueryConnector\JSONLibrary.jar" -d $(COMP_CLASS_OUT) $(COMP_JAVAFILES) 

#script

$(CLASSFILES) : $(JAVAFILES) 
	-$(MKDIR) $(subst /,$(PS),$(@D))
	$(SDK_JAVAC) $(JAVAC_FLAGS) -classpath "$(SDK_CLASSPATH);QueryConnector\LODBConnector.jar;QueryConnector\JSONLibrary.jar" -d $(SCRIPT_CLASS_OUT) $(JAVAFILES)

# ----------------------------------------------------------------------
	
# rule for component jar file
$(COMP_JAR) : $(COMP_JAR_MANIFEST) $(COMP_CLASSFILES) $(CLASSFILES)
	-$(DEL) $(subst \\,\,$(subst /,$(PS),$@))
	-$(MKDIR) $(subst /,$(PS),$(@D))
	-$(COPY) $(CLASSFILES_FOR_COMP) $(COMP_CLASS_OUT)
	cd $(subst /,$(PS),$(COMP_CLASS_OUT)) && $(SDK_JAR) cvfm $(@F) $(<F) $($(basename $(basename $(@F)))_CLASSFILES) QueryConnector$$Query.class QueryConnector$$Settings.class QueryConnector.class
	
$(SCRIPT_JAR) : $(CLASSFILES)
	-$(DEL) $(subst \\,\,$(subst /,$(PS),$@))
	-$(MKDIR) $(subst /,$(PS),$(@D))
	$(SDK_JAR) cvf $@ $(CLASSNAMES) -C $(SCRIPT_CLASS_OUT) .

# ----------------------------------------------------------------------
	
# rule for component package file
$(SCRIPT_PACKAGE) : $(SCRIPT_JAR) META-INF/manifest.xml $(SCRIPT_NAME)/parcel-descriptor.xml $(COMP_RDB) $(COMP_JAR)
	-$(DEL) $(subst \\,\,$(subst /,$(PS),$@))
	-$(MKDIR) $(subst /,$(PS),$(@D))
	$(SDK_ZIP) $@ META-INF/manifest.xml $(COMP_NAME).components $(SCRIPT_NAME)/parcel-descriptor.xml description.xml Addons.xcu $(SCRIPT_NAME)/LODBConnector.jar $(SCRIPT_NAME)/JSONLibrary.jar description/desc_it.txt description/desc_en.txt images/noimage_16.bmp license/lgpl.txt icons/queryconnector.png
	cd $(OUT_CLASS) && $(SDK_ZIP) -u ../../$(OUT_BIN)/$(@F) $(SAMPLE_NAME)/$(SCRIPT_JAR_NAME) 
	cd $(subst /,$(PS),$(COMP_GEN_OUT)) && $(SDK_ZIP) -u ../../bin/$(@F) $(COMP_RDB_NAME)
	cd $(subst /,$(PS),$(COMP_CLASS_OUT)) && $(SDK_ZIP) -u ../../bin/$(@F) $(COMP_JAR_NAME)

$(SCRIPT_REGISTERFLAG) : $(SCRIPT_PACKAGE)
ifeq "$(SDK_AUTO_DEPLOYMENT)" "YES"
	-$(DEL) $(SCRIPT_REGISTERFLAG)
	-$(MKDIR) $(subst /,$(PS),$(@D))
	$(DEPLOYTOOL) $(SCRIPT_PACKAGE_URL)
	@echo flagged > $(subst /,$(PS),$@)
else
	@echo --------------------------------------------------------------------------------
	@echo  If you want to install your component automatically, please set the environment
	@echo  variable SDK_AUTO_DEPLOYMENT = YES. But note that auto deployment is only 
	@echo  possible if no office instance is running. 
	@echo --------------------------------------------------------------------------------
endif


QueryConnectorMake: $(SCRIPT_REGISTERFLAG)
	@echo --------------------------------------------------------------------------------
	@echo The "$(QM)$(SCRIPT_NAME)$(QM)" scripting package was installed in your office installation 
	@echo if SDK_AUTO_DEPLOYMENT = YES. If you want to run the script, start the Office 
	@echo and use the Tools/Macros/Run Macro menu item.
	@echo --------------------------------------------------------------------------------

%.run: $(CLASSFILES)
	$(SDK_JAVA) -classpath "$(SDK_CLASSPATH)" $(subst /,.,$(PACKAGE)).$(basename $@)

%.dbg: $(JAR1_JAR)
	jdb -classpath "$(SDK_CLASSPATH)" $(subst /,.,$(PACKAGE)).$(basename $@)

.PHONY: clean
clean :
	-$(DELRECURSIVE) $(subst /,$(PS),$(SAMPLE_CLASS_OUT))
	-$(DELRECURSIVE) $(subst /,$(PS),$(SCRIPT_GEN_OUT))
	-$(DELRECURSIVE) $(subst /,$(PS),$(COMP_GEN_OUT))
	-$(DELRECURSIVE) $(subst /,$(PS),$(COMP_CLASS_OUT))
	-$(DEL) $(subst \\,\,$(subst /,$(PS),$(SCRIPT_PACKAGE_URL)))

