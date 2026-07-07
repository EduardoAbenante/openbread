import test from 'node:test';
import assert from 'node:assert/strict';
import { buildOperarioPayload, createNewOperarioForm } from './operariosLogic.js';

test('buildOperarioPayload maps form values to backend payload', () => {
  const payload = buildOperarioPayload({
    id: 7,
    nif: '12345678Z',
    name: 'Ana',
    surname: 'García',
    email: 'ana@example.com',
    password: 'Secret1!',
    role: 'ADMIN',
    phone: '600123456',
    postalCode: '28001',
    active: true,
  });

  assert.deepEqual(payload, {
    id: 7,
    nif: '12345678Z',
    name: 'Ana',
    surname: 'García',
    email: 'ana@example.com',
    password: 'Secret1!',
    role: 'ADMIN',
    phone: '600123456',
    postalCode: '28001',
    active: true,
  });
});

test('createNewOperarioForm returns sensible defaults', () => {
  const form = createNewOperarioForm();

  assert.equal(form.nif, '');
  assert.equal(form.name, '');
  assert.equal(form.surname, '');
  assert.equal(form.email, '');
  assert.equal(form.role, 'USER');
  assert.equal(form.active, true);
  assert.equal(form.photoFile, null);
  assert.equal(form.photoUrl, null);
});
