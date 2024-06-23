FROM clojure:tools-deps

WORKDIR /usr/src/app

# RUN apk add --no-cache rlwrap=0.46.1-r0

COPY src src
COPY test test
COPY resources resources
COPY dev dev
COPY deps.edn deps.edn
COPY .env .env

EXPOSE 3000
CMD ["clj", "-M", "-m", "retromiks.core"]

# docker build . --tag retromiks
# docker run --rm retromiks:latest
