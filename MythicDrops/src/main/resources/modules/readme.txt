The folder this file resides in is used for loading MythicDrops modules. Simply place a MythicDrops module into this
  folder and the plugin will automatically load it.

########
Making a MythicDrops module
########
Making a MythicDrops module is relatively simple. The class must extend a certain class
  (net.nunnerycode.bukkit.libraries.module.Module) and be packaged as a JAR containing a module.yml that has the
  following fields contained in it:

  name: module name here
  authors: {comma, delimited, list, of, authors, here}
  description: module description here
  version: module version here
  mainClass: fully qualified name (package and class name) of class that extends Module

Once you've done that, you've finished making your module. To test it, simply drop it in the MythicDrops module folder.

Enjoy!