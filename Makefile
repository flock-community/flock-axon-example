.PHONY: *

## Equivalent to `make clean build`
all: clean build

build: ## build and verify the project
	./mvnw verify -Pformat

clean: ## maven clean
	./mvnw clean

down: ## docker compose down
	docker compose down

run: ## run the app
	java -jar app/target/app-*.jar

up: ## docker compose up
	docker compose up -d

update: ## update the versions with maven
	./mvnw versions:update-parent versions:update-properties versions:use-latest-versions $(options)

yolo: ## quick build without qa
	./mvnw verify -Pformat -DskipTests

# This outputs any command in the Makefile. With a short description taken from a ## prefixed command after the command (preferred) or the line above
help: ## Show this help
	@echo "Usage: make <command>"; \
	echo ""; \
	desc=""; \
	while IFS= read -r line; do \
		case "$$line" in \
			'## '*)              desc="$${line#\#\# }" ;; \
			[a-zA-Z_-]*:*'## '*) printf '\033[36m%-20s\033[0m %s\n' "$${line%%:*}" "$${line#*\#\# }"; desc="" ;; \
			[a-zA-Z_-]*:*)       printf '\033[36m%-20s\033[0m %s\n' "$${line%%:*}" "$$desc"; desc="" ;; \
			*)                   desc="" ;; \
		esac; \
	done < $(MAKEFILE_LIST) | sort
