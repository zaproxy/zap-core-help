<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE helpset PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN" "http://java.sun.com/products/javahelp/helpset_2_0.dtd">
<helpset version="2.0" xml:lang="sr-CS">

  <!-- title -->
  <title>ZAP by Checkmarx - Desktop User Guide</title>

  <!-- maps -->
  <maps>
     <homeID>zap.intro</homeID>
     <mapref location="map.jhm"/>
  </maps>

  <!-- views -->
  <view mergetype="org.zaproxy.zap.extension.help.ZapTocMerger">
    <name>TOC</name>
    <label>Contents</label>
    <type>org.zaproxy.zap.extension.help.ZapTocView</type>
    <data>toc.xml</data>
  </view>

  <view mergetype="javax.help.SortMerge">
    <name>Index</name>
    <label>Index</label>
    <type>javax.help.IndexView</type>
    <data>index.xml</data>
  </view>

  <view>
    <name>Search</name>
    <label>Search</label>
    <type>org.zaproxy.zap.extension.help.ZapSearchView</type>
    <data engine="com.sun.java.help.search.DefaultSearchEngine">
      JavaHelpSearch
    </data>
  </view>

  <view>
    <name>Favorites</name>
    <label>Favorites</label>
    <type>javax.help.FavoritesView</type>
  </view>

  <presentation default="true" displayviewimages="false">
     <name>main window</name>
     <size height="600" width="1000"/>
     <location x="200" y="200"/>
     <title>ZAP - Desktop User Guide</title>
     <image>toplevelicon</image>
     <toolbar>
	<helpaction>javax.help.BackAction</helpaction>
	<helpaction>javax.help.ForwardAction</helpaction>
	<helpaction>javax.help.SeparatorAction</helpaction>
	<helpaction>javax.help.HomeAction</helpaction>
	<helpaction>javax.help.ReloadAction</helpaction>
	<helpaction>javax.help.SeparatorAction</helpaction>
	<helpaction>javax.help.PrintAction</helpaction>
	<helpaction>javax.help.PrintSetupAction</helpaction>
     </toolbar>
  </presentation>
  <presentation>
     <name>main</name>
     <size height="400" width="400"/>
     <location x="200" y="200"/>
     <title>ZAP - Desktop User Guide</title>
  </presentation>
</helpset>