import test from 'node:test';
import assert from 'node:assert/strict';
import { createAvatarUploadRequest } from './OperariosApi.js';
import { resolveUserId } from './OperariosApi.compat.js';

test('createAvatarUploadRequest builds a FormData payload without forcing multipart content type', () => {
  const file = new File(['avatar'], 'avatar.png', { type: 'image/png' });
  const request = createAvatarUploadRequest(file);

  assert.ok(request.formData instanceof FormData);
  assert.equal(request.formData.get('avatarFile').name, 'avatar.png');
  assert.equal(request.config.headers['Content-Type'], undefined);
});

test('resolveUserId handles numeric and object backend responses', () => {
  assert.equal(resolveUserId(42), 42);
  assert.equal(resolveUserId({ id: 77 }), 77);
  assert.equal(resolveUserId({ userId: 88 }), 88);
});
