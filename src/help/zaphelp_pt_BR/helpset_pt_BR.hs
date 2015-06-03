<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE helpset PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN" "http://java.sun.com/products/javahelp/helpset_2_0.dtd">
<helpset version="2.0" xml:lang="pt-BR">

  <!-- title -->
  <title>ZAP - Guia do Usuário</title>

  <!-- maps -->
  <maps>
     <homeID>top</homeID>
     <mapref location="map.jhm"/>
  </maps>

  <!-- views -->
  <view mergetype="org.zaproxy.zap.extension.help.ZapTocMerger">
    <name>TOC</name>
    <label>Conteúdo</label>
    <type>org.zaproxy.zap.extension.help.ZapTocView</type>
    <data>toc.xml</data>
  </view>

  <view mergetype="javax.help.SortMerge">
    <name>Index</name>
    <label>Índice</label>
    <type>javax.help.IndexView</type>
    <data>index.xml</data>
  </view>

  <view>
    <name>Search</name>
    <label>Localizar</label>
    <type>javax.help.SearchView</type>
    <data engine="com.sun.java.help.search.DefaultSearchEngine">
      JavaHelpSearch
    </data>
  </view>

  <view>
    <name>Favorites</name>
    <label>Favoritos</label>
    <type>javax.help.FavoritesView</type>
  </view>

  <presentation default="true" displayviewimages="false">
     <name>main window</name>
     <size height="600" width="1000"/>
     <location x="200" y="200"/>
     <title>ZAP - Guia do Usuário</title>
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
     <title>ZAP - Guia do Usuário</title>
  </presentation>
</helpset>