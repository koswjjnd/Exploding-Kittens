# Instructor Project Meeting Minutes

**Time**: Week 5

**Attendees**: (4 out of 5 team members --- Dr. Yiji is terribly sorry as she forgot to take attendance during the meeting...)

**Note Taker**: Dr. Yiji Zhang


## Agenda
### Progress Evaluation
- produced the class diagram
- attempted to split tasks by feature, ran into some collaboration issues (see notes below)

### Questions from the team (about design, linter, tests, etc.)
1. collaboration problem: takeaways
- The team's effort to split the tasks by feature definitely makes sense and is a good start!!
- The team needs to further determine the design as well --- what classes to have, what public methods to have (and their specification, like the contents we put in a Java doc indeed)
- then, we can split the tasks by class or even methods, and no one needs to wait for anyone else to complete because we can (and should) use mocking to ensure each unit works on its own first anyways.

2. review the design:
- Consider combining Game and GameData class into one? Because currently Game class doesn't have any methods.
- Review the event sequence diagram and make sure it faithfully describes what the team meant. For instance, "initialize turn order" probably should have been "player is eliminated"?

3. Clarification on how to do BVA + TDD.
- Suppose the team has decided on the design and you are assigned a class to implement
- One commit for the BVA analysis of this class. Document it in docs/BVA/bva-<class_name>.md
- Then, for EVERY test case that is identified by BVA, one commit per each PASSING test case (just like Lab 3 and the in-class Fib example).

4. Run checkstyle locally
- See the changed build script --- now the `gradle test` task always runs checkstyle as well
- Alternatively, you can also always run `gradle build`. 

### Teamwork
#### Strength: What has been working well
didn't have time to evaluate

#### Concerns: What can be improved
didn't have time to evaluate

#### Notes
1. Make sure to do `git pull origin main` often so the feature branch is up to date of the current state of the project.
2. Make sure to structure your project using the MVC pattern (see GUI Development session) --- it will make testing and development a lot easier, and make SpotBug errors easier to fix (as many bad coding practices are results of poor design)
3. Run SpotBugs and Checkstyle locally so you can fix the issue before pushing them into the repository (how: Gradle icon --> Tasks --> verification --> test, if the build script is all set up)



## Instruction: After the meeting, please confirm/review the minutes as a team. Add additional notes if needed. Then merge the PR.