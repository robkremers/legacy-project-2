FROM node:16.13.2 AS compile-image

# Install Chrome
RUN echo 'deb http://dl.google.com/linux/chrome/deb/ stable main' > /etc/apt/sources.list.d/chrome.list
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN set -x \
    && apt-get update \
    && apt-get install -y \
        google-chrome-stable

ENV CHROME_BIN "/usr/bin/google-chrome"

# Done chrome installation

RUN npm install -g @angular/cli -y --force

WORKDIR /opt/ng
COPY package.json package-lock.json ./

RUN npm cache clean --force

RUN npm ci
COPY . ./

ENV PATH="./node_modules/.bin:$PATH"
RUN ng lint
RUN cp karma.conf-docker.js karma.conf.js && ng test --browsers BKWIChromeHeadless
RUN ng build --aot # --deploy-url=/mt/ --base-href=/mt/

FROM nginx:alpine AS runtime
COPY docker/nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=compile-image /opt/ng/dist/gebruikers-administratie-frontend-angular /usr/share/nginx/html
