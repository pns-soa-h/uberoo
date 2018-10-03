FROM alpine:latest
RUN apk update
RUN apk add curl
RUN apk add jq
ADD scenario.sh ./scenario.sh
CMD ["sh", "scenario.sh"]