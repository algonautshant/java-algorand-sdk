package algod

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// HealthCheck /health
// Returns OK if healthy.
type HealthCheck struct {
	c *Client
}

// Do performs HTTP request
func (s *HealthCheck) Do(ctx context.Context,
	headers ...*common.Header) (response models.String, err error) {
	err = s.c.get(ctx, &response,
		"/health", nil, headers)
	return
}
