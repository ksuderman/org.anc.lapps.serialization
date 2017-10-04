BRANCH=$(shell git branch | grep \* | cut -d\  -f2)
GROOVYDOC=src/test/resources/lappsdoc

help:
	@echo "Help is needed..."
	
clean:
	mvn clean
	
jar:
	mvn package
	
deploy:
	pom ; if [ $$? -eq 1 ] ; then exit 1 ; fi
	mvn javadoc:jar source:jar deploy

snapshot:
	issnapshot ; if [ $$? -eq 1 ] ; then exit 1 ; fi
	mvn javadoc:jar source:jar deploy

docs:
	if [ -e target/apidocs ] ; then rm -rf target/apidocs ; fi
	$(GROOVYDOC) "Lappsgrid Serialization"

site:
	git stash
	if [ -e target/apidocs ] ; then rm -rf target/apidocs ; fi
	$(GROOVYDOC) "Lappsgrid Serialization"
	git checkout gh-pages
	rm *.html *.ico *.gif
	rm -rf org
	git commit -a -m "Removed old files."
	cp -r target/apidocs/* .	
	git add *.html *.ico *.gif org
	git commit -a -m "Updated gh-pages."
	git push origin gh-pages
	git checkout $(BRANCH)
	git stash apply

test:
	echo "Branch is $(BRANCH)"

