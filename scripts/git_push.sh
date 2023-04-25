###################################################
# Script for git add, commit & push operation v1.0
###################################################

total_args=2

if (($# != 2)); then
	echo "(e) Expecting ${total_args} arguments!"
	echo "(i) 1. \"<message>\" (with quotes!)"
	echo "(i) 2. <git_branch_name>"
else

	current_position=`pwd`
	
	cd ..
	
	git status
	git add .
	git commit -m "$1"
	git push origin "$2"

	cd $current_position
fi

