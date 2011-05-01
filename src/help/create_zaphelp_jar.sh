#!/bin/sh
#
# $Id$
#
# See: http://code.google.com/p/zaproxy/wiki/AuthoringHelp
#

cd zaphelp
jhindexer zaphelp
jar -cf ../zaphelp.jar *
cd ..
mv zaphelp.jar ../../lib/