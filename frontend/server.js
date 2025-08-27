const http = require('http');
const fs = require('fs');
const path = require('path');
const { Server } = require('socket.io');

const PORT = 8000;

const server = http.createServer((req, res) => {
  let filePath = '.' + req.url;
  if (filePath === './') filePath = './stream.html';

  const extname = path.extname(filePath).toLowerCase();
  const mimeTypes = {
    '.html': 'text/html',
    '.js': 'text/javascript',
    '.css': 'text/css',
    '.mp4': 'video/mp4',
  };
  const contentType = mimeTypes[extname] || 'application/octet-stream';

  if (extname === '.mp4') {
    // Phần xử lý range cho video
    fs.stat(filePath, (err, stats) => {
      if (err) {
        res.writeHead(404);
        res.end('Not found');
        return;
      }

      let range = req.headers.range;
      if (!range) {
        res.writeHead(416, { 'Content-Range': `bytes */${stats.size}` });
        res.end();
        return;
      }

      const positions = range.replace(/bytes=/, '').split('-');
      const start = parseInt(positions[0], 10);
      const end = positions[1] ? parseInt(positions[1], 10) : stats.size - 1;
      const chunksize = (end - start) + 1;

      res.writeHead(206, {
        'Content-Range': `bytes ${start}-${end}/${stats.size}`,
        'Accept-Ranges': 'bytes',
        'Content-Length': chunksize,
        'Content-Type': 'video/mp4',
      });

      const stream = fs.createReadStream(filePath, { start, end });
      stream.pipe(res);
    });
  } else {
    fs.readFile(filePath, (err, content) => {
      if (err) {
        res.writeHead(404);
        res.end('Not found');
      } else {
        res.writeHead(200, { 'Content-Type': contentType });
        res.end(content);
      }
    });
  }
});

const io = new Server(server);

io.on('connection', (socket) => {
  console.log('User connected:', socket.id);

  socket.on('chat message', (msg) => {
    io.emit('chat message', msg); // phát đến tất cả
  });

  socket.on('disconnect', () => {
    console.log('User disconnected:', socket.id);
  });
});

server.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});
