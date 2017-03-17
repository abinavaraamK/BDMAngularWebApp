@echo on
cls

set PATH=%PATH%;E:\BuildFaces\apache-ant-1.7.0\bin
ant -buildfile %2 %1%